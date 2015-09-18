package com.coolweather.app.util;

import android.text.TextUtils;

import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;

public class Utility {
	/**
	 * 解析和处理服务器返回的省级数据
	 */
	public synchronized static boolean handleProvincesResponse(
			CoolWeatherDB coolWeatherDB, String response) {
		if (!TextUtils.isEmpty(response)) {
			String[] allProvinces = response.split(",");
			if (null != allProvinces && allProvinces.length > 0) {
				for (String p : allProvinces) {
					String[] array = p.split("\\|");
					Province province = new Province();
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);
					// 存储
					coolWeatherDB.saveProvince(province);
				}
				return true;
			}
		}

		return false;

	}

	/**
	 * 解析和处理服务器返回的市级数据
	 */
	public synchronized static boolean handleCitiesResponse(
			CoolWeatherDB coolWeatherDB, String response, int provinceId) {
		if (!TextUtils.isEmpty(response)) {
			String[] allCities = response.split(",");
			if (null != allCities && allCities.length > 0) {
				for (String c : allCities) {
					String[] array = c.split("\\|");
					City entity = new City();
					entity.setCityCode(array[0]);
					entity.setCityName(array[1]);
					entity.setProvinceId(provinceId);
					// 存储
					coolWeatherDB.saveCity(entity);
				}
				return true;
			}
		}

		return false;
	}

	/**
	 * 解析和处理服务器返回的县级数据
	 */
	public synchronized static boolean handleCountiesResponse(
			CoolWeatherDB coolWeatherDB, String response, int cityId) {
		if (!TextUtils.isEmpty(response)) {
			String[] allCounties = response.split(",");
			if (null != allCounties && allCounties.length > 0) {
				for (String c : allCounties) {
					String[] array = c.split("\\|");
					County entity = new County();
					entity.setCountyCode(array[0]);
					entity.setCountyName(array[1]);
					entity.setCityId(cityId);
					// 存储
					coolWeatherDB.saveCounty(entity);
				}
				return true;
			}
		}

		return false;

	}
}
