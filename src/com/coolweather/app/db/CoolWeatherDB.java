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
	 * 数据库名
	 */
	public static final String DB_NAME = "cool_weather";
	/**
	 * 数据库版本
	 */
	public static final int VERSION = 1;
	private static CoolWeatherDB mCoolWeatherDB;
	private SQLiteDatabase db;

	/**
	 * 构造方法私有化
	 */
	private CoolWeatherDB(Context context) {
		CoolWeatherDBHelper dbHelper = new CoolWeatherDBHelper(context,
				DB_NAME, null, VERSION);
		db = dbHelper.getWritableDatabase();
	}

	/**
	 * 获取CoolWeatherDB实例
	 */
	public synchronized static CoolWeatherDB getInstance(Context context) {
		if (null == mCoolWeatherDB) {
			mCoolWeatherDB = new CoolWeatherDB(context);
		}
		return mCoolWeatherDB;
	}

	/**
	 * 将Province实例存储到数据库
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
	 * 从数据库读出所有Province信息
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
	 * 将City实例存储到数据库
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
	 * 从数据库读出某个Province下所有City信息
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
	 * 将County实例存储到数据库
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
	 * 从数据库读出某个City下所有County信息
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
