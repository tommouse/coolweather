package com.coolweather.app.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;

public class Utility {
	/**
	 * �����ʹ�����������ص�ʡ������
	 */
	public synchronized static boolean handleProvincesResponse(
			CoolWeatherDB coolWeatherDB, String response) {
		if (!TextUtils.isEmpty(response)) {
			String[] allProvinces = response.split(",");
			if (null != allProvinces && allProvinces.length > 1) {
				for (String p : allProvinces) {
					String[] array = p.split("\\|");
					Province province = new Province();
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);
					// �洢
					coolWeatherDB.saveProvince(province);
				}
				return true;
			}
		}

		return false;

	}

	/**
	 * �����ʹ�����������ص��м�����
	 */
	public synchronized static boolean handleCitiesResponse(
			CoolWeatherDB coolWeatherDB, String response, int provinceId) {
		if (!TextUtils.isEmpty(response)) {
			String[] allCities = response.split(",");
			if (null != allCities && allCities.length > 1) {
				for (String c : allCities) {
					String[] array = c.split("\\|");
					City entity = new City();
					entity.setCityCode(array[0]);
					entity.setCityName(array[1]);
					entity.setProvinceId(provinceId);
					// �洢
					coolWeatherDB.saveCity(entity);
				}
				return true;
			}
		}

		return false;
	}

	/**
	 * �����ʹ�����������ص��ؼ�����
	 */
	public synchronized static boolean handleCountiesResponse(
			CoolWeatherDB coolWeatherDB, String response, int cityId) {
		if (!TextUtils.isEmpty(response)) {
			String[] allCounties = response.split(",");
			if (null != allCounties && allCounties.length > 1) {
				for (String c : allCounties) {
					String[] array = c.split("\\|");
					County entity = new County();
					entity.setCountyCode(array[0]);
					entity.setCountyName(array[1]);
					entity.setCityId(cityId);
					// �洢
					coolWeatherDB.saveCounty(entity);
				}
				return true;
			}
		}

		return false;

	}

	/**
	 * �������������ص�Json����,���洢������
	 */
	public static void handleWeatherResponse(Context context, String response) {
		try {
			JSONObject jsonObject = new JSONObject(response);
			JSONObject jsonWeather = jsonObject.getJSONObject("weatherinfo");
			String cityName = jsonWeather.getString("city");
			String weatherCode = jsonWeather.getString("cityid");
			String temp1 = jsonWeather.getString("temp1");
			String temp2 = jsonWeather.getString("temp2");
			String weatherDesp = jsonWeather.getString("weather");
			String publishTime = jsonWeather.getString("ptime");
			saveWeatherInfo(context, cityName, weatherCode, temp1, temp2,
					weatherDesp, publishTime);
		} catch (JSONException je) {
		}
	}

	/**
	 * �������������ص��������� �洢��SharedPreferences�ļ��С�
	 */
	public static void saveWeatherInfo(Context context, String cityName,
			String weatherCode, String temp1, String temp2, String weatherDesp,
			String publishTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy��M��d��", Locale.CHINA);
		SharedPreferences.Editor editor = PreferenceManager
				.getDefaultSharedPreferences(context).edit();
		editor.putBoolean("city_selected", true);
		editor.putString("city_name", cityName);
		editor.putString("weather_code", weatherCode);
		editor.putString("temp1", temp1);
		editor.putString("temp2", temp2);
		editor.putString("weather_desp", weatherDesp);
		editor.putString("publish_time", publishTime);
		editor.putString("current_date", sdf.format(new Date()));
		editor.commit();
	}
}
