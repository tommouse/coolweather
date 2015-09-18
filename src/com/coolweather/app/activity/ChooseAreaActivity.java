package com.coolweather.app.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.coolweather.app.R;
import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;

public class ChooseAreaActivity extends Activity {
	public static final int LEVEL_PROVINCE = 0;
	public static final int LEVEL_CITY = 1;
	public static final int LEVEL_COUNTY = 2;

	private ProgressDialog progressDialog;
	private TextView titleText;
	private ListView listView;
	private ArrayAdapter<String> adapter;
	private List<String> dataList = new ArrayList<String>();
	private CoolWeatherDB coolWeatherDB;

	/**
	 * ʡ�б�
	 */
	private List<Province> provinceList;
	/**
	 * ���б�
	 */
	private List<City> cityList;
	/**
	 * ���б�
	 */
	private List<County> countyList;
	/**
	 * ѡ�е�ʡ��
	 */
	private Province selectedProvince;
	/**
	 * ѡ�е���
	 */
	private City selectedCity;
	/**
	 * ��ǰѡ�еļ���
	 */
	private int selectedLevel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		titleText = (TextView) findViewById(R.id.title_text);
		listView = (ListView) findViewById(R.id.list_view);
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, dataList);
		listView.setAdapter(adapter);
		coolWeatherDB = CoolWeatherDB.getInstance(this);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (selectedLevel == LEVEL_PROVINCE) {
					selectedProvince = provinceList.get(position);
					queryCities();
				} else if (selectedLevel == LEVEL_CITY) {
					selectedCity = cityList.get(position);
					queryCounties();
				}
			}
		});
		queryProvinces();// ����ʡ������
	}

	/**
	 * ��ѯȫ��ʡ�����ݣ����ȴ����ݣ����û�д������ѯ
	 */
	private void queryProvinces() {
		provinceList = coolWeatherDB.loadProvinces();
		if (provinceList.size() > 0) {
			dataList.clear();
			for (Province entity : provinceList) {
				dataList.add(entity.getProvinceName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText("�й�");
			selectedLevel = LEVEL_PROVINCE;
		} else {
			queryFromServer(null, "province");
		}
	}

	/**
	 * ��ѯѡ��ʡ��ȫ���м����ݣ����ȴ����ݣ����û�д������ѯ
	 */
	private void queryCities() {
		cityList = coolWeatherDB.loadCities(selectedProvince.getId());
		if (cityList.size() > 0) {
			dataList.clear();
			for (City entity : cityList) {
				dataList.add(entity.getCityName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectedProvince.getProvinceName());
			selectedLevel = LEVEL_CITY;
		} else {
			queryFromServer(selectedProvince.getProvinceCode(), "city");
		}
	}

	/**
	 * ��ѯѡ�����������ؼ����ݣ����ȴ����ݣ����û�д������ѯ
	 */
	private void queryCounties() {
		countyList = coolWeatherDB.loadCounties(selectedCity.getId());
		if (countyList.size() > 0) {
			dataList.clear();
			for (County entity : countyList) {
				dataList.add(entity.getCountyName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			titleText.setText(selectedCity.getCityName());
			selectedLevel = LEVEL_COUNTY;
		} else {
			queryFromServer(selectedCity.getCityCode(), "county");
		}
	}

	/**
	 * ���ݴ��ź����ʹ������ѯʡ������Ϣ
	 */
	private void queryFromServer(String code, final String type) {
		String address;
		if (!TextUtils.isEmpty(code)) {
			address = "http://www.weather.com.cn/data/list3/city" + code
					+ ".xml";
		} else {
			address = "http://www.weather.com.cn/data/list3/city.xml";
		}
		showProgressDialog();
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {

			@Override
			public void onFinish(String response) {
				boolean result = false;
				if ("province".equals(type)) {
					result = Utility.handleProvincesResponse(coolWeatherDB,
							response);
				} else if ("city".equals(type)) {
					result = Utility.handleCitiesResponse(coolWeatherDB,
							response, selectedProvince.getId());
				} else if ("county".equals(type)) {
					result = Utility.handleCountiesResponse(coolWeatherDB,
							response, selectedCity.getId());
				}
				if (result) {
					// ͨ��runOnUiThread�����ᵽ���߳�
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							closeProgressDialog();
							if ("province".equals(type)) {
								queryProvinces();
							} else if ("city".equals(type)) {
								queryCities();
							} else if ("county".equals(type)) {
								queryCounties();
							}
						}
					});
				}
			}

			@Override
			public void onError(final Exception e) {
				// ͨ��runOnUiThread�����ᵽ���߳�
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						closeProgressDialog();
						Toast.makeText(ChooseAreaActivity.this, "����ʧ��",
								Toast.LENGTH_SHORT).show();
						Log.e("error", e.getMessage(), e);
					}

				});
			}
		});
	}

	/**
	 * ��ʾ�Ի���
	 */
	private void showProgressDialog() {
		if (null == progressDialog) {
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("���ڼ���...");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}

	/**
	 * �رնԻ���
	 */
	private void closeProgressDialog() {
		if (null != progressDialog) {
			progressDialog.dismiss();
		}
	}

	/**
	 * ����Back�������ݵ�ǰѡ��ļ����жϣ��������б�ʡ�б������˳�
	 */
	@Override
	public void onBackPressed() {
		if (selectedLevel == LEVEL_COUNTY) {
			queryCities();
		} else if (selectedLevel == LEVEL_CITY) {
			queryProvinces();
		} else {
			finish();
		}
	}
}
