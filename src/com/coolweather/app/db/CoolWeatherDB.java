package com.coolweather.app.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;

public class CoolWeatherDB {
	/**
	 * ���ݿ���
	 */
	public static final String DB_NAME = "cool_weather";
	/**
	 * ���ݿ�汾
	 */
	public static final int VERSION = 1;
	private static CoolWeatherDB mCoolWeatherDB;
	private SQLiteDatabase db;

	/**
	 * ���췽��˽�л�
	 */
	private CoolWeatherDB(Context context) {
		CoolWeatherDBHelper dbHelper = new CoolWeatherDBHelper(context,
				DB_NAME, null, VERSION);
		db = dbHelper.getWritableDatabase();
	}

	/**
	 * ��ȡCoolWeatherDBʵ��
	 */
	public synchronized static CoolWeatherDB getInstance(Context context) {
		if (null == mCoolWeatherDB) {
			mCoolWeatherDB = new CoolWeatherDB(context);
		}
		return mCoolWeatherDB;
	}

	/**
	 * ��Provinceʵ���洢�����ݿ�
	 */
	public void saveProvince(Province entity) {
		if (null != entity) {
			ContentValues values = new ContentValues();
			values.put("province_name", entity.getProvinceName());
			values.put("province_code", entity.getProvinceCode());
			db.insert("Province", null, values);
		}
	}

	/**
	 * �����ݿ��������Province��Ϣ
	 */
	public List<Province> loadProvinces() {
		List<Province> list = new ArrayList<Province>();
		Cursor cursor = db
				.query("Province", null, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				Province entity = new Province();
				entity.setId(cursor.getInt(cursor.getColumnIndex("id")));
				entity.setProvinceName(cursor.getString(cursor
						.getColumnIndex("province_name")));
				entity.setProvinceCode(cursor.getString(cursor
						.getColumnIndex("province_code")));
				list.add(entity);
			} while (cursor.moveToNext());
		}
		return list;
	}

	/**
	 * ��Cityʵ���洢�����ݿ�
	 */
	public void saveCity(City entity) {
		if (null != entity) {
			ContentValues values = new ContentValues();
			values.put("city_name", entity.getCityName());
			values.put("city_code", entity.getCityCode());
			values.put("province_id", entity.getProvinceId());
			db.insert("City", null, values);
		}
	}

	/**
	 * �����ݿ����ĳ��Province������City��Ϣ
	 */
	public List<City> loadCities(int provinceId) {
		List<City> list = new ArrayList<City>();
		Cursor cursor = db.query("City", null, "province_id=?",
				new String[] { String.valueOf(provinceId) }, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				City entity = new City();
				entity.setId(cursor.getInt(cursor.getColumnIndex("id")));
				entity.setCityName(cursor.getString(cursor
						.getColumnIndex("city_name")));
				entity.setCityCode(cursor.getString(cursor
						.getColumnIndex("city_code")));
				entity.setProvinceId(provinceId);
				list.add(entity);
			} while (cursor.moveToNext());
		}
		return list;
	}

	/**
	 * ��Countyʵ���洢�����ݿ�
	 */
	public void saveCounty(County entity) {
		if (null != entity) {
			ContentValues values = new ContentValues();
			values.put("county_name", entity.getCountyName());
			values.put("county_code", entity.getCountyCode());
			values.put("city_id", entity.getCityId());
			db.insert("County", null, values);
		}
	}

	/**
	 * �����ݿ����ĳ��City������County��Ϣ
	 */
	public List<County> loadCounties(int cityId) {
		List<County> list = new ArrayList<County>();
		Cursor cursor = db.query("County", null, "city_id=?",
				new String[] { String.valueOf(cityId) }, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				County entity = new County();
				entity.setId(cursor.getInt(cursor.getColumnIndex("id")));
				entity.setCountyName(cursor.getString(cursor
						.getColumnIndex("county_name")));
				entity.setCountyCode(cursor.getString(cursor
						.getColumnIndex("county_code")));
				entity.setCityId(cityId);
				list.add(entity);
			} while (cursor.moveToNext());
		}
		return list;
	}
}
