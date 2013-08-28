package com.service.freetalk.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

public class ServletControllerTest {
	public void userListTest() {
		URL url;
		HttpURLConnection conn = null;
		try {
			JSONObject json = new JSONObject();
			json.put("userId", "admin");

			url = new URL("http://192.168.0.5:8080/FreeTalkService"
					+ "/user/userList.do");
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(30 * 1000);
			conn.setReadTimeout(30 * 1000);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded;charset=UTF-8");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			OutputStream os = conn.getOutputStream();
			os.write(json.toString().getBytes());
			os.flush();
			os.close();

			int responseCode = conn.getResponseCode();
			if (HttpURLConnection.HTTP_OK == responseCode) {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						conn.getInputStream()));
				StringBuffer buf = new StringBuffer();
				while (true) {
					String str = br.readLine();
					if (null == str) {
						break;
					}
					buf.append(str);

				}
				String resultJson = new JSONObject(buf.toString())
						.getString("result");

				// JSONArray jsonArray = resultJson.getJSONArray("list");
				ObjectMapper mapper = new ObjectMapper();
				List<Map<String, String>> userList = mapper.readValue(
						resultJson.toString(),
						new TypeReference<List<Map<String, String>>>() {
						});

				System.out.println(userList);

			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void registerIdTest() {
		URL url;
		HttpURLConnection conn = null;
		try {
			url = new URL("http://192.168.0.5:8080/FreeTalkService"
					+ "/device/register.do");
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded;charset=UTF-8");
			conn.setDoInput(true);
			conn.setDoOutput(true);

			OutputStream os = conn.getOutputStream();
			JSONObject json = new JSONObject();
			json.put("regId", "4321");
			json.put("userId", "admin");
			os.write(json.toString().getBytes());
			os.flush();
			os.close();

			int responseCode = conn.getResponseCode();
			if (HttpURLConnection.HTTP_OK == responseCode) {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						conn.getInputStream()));
				StringBuffer buf = new StringBuffer();
				while (true) {
					String str = br.readLine();
					if (null == str) {
						break;
					}
					buf.append(str);
				}
			} else {
			}
			conn.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			conn.disconnect();
		}
	}
}
