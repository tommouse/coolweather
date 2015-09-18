package com.coolweather.app.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {

	public static void sendHttpRequest(final String spec,
			final HttpCallbackListener listener) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				HttpURLConnection connection = null;
				try {
					URL url = new URL(spec);
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
					InputStream in = connection.getInputStream();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(in));
					StringBuilder response = new StringBuilder();
					String line;
					while (null != (line = reader.readLine())) {
						response.append(line);
					}
					// �ص�
					if (null != listener) {
						listener.onFinish(response.toString());
					}
				} catch (Exception e) {
					if (null != listener) {
						listener.onError(e);
					}
				} finally {
					if (null != connection) {
						connection.disconnect();
					}
				}
			}
		}).start();

	}
}
