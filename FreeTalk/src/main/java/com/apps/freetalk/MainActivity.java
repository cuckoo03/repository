package com.apps.freetalk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.apps.freetalk.config.ServerConfig;
import com.apps.freetalk.config.SystemConfig;
import com.google.android.gcm.GCMRegistrar;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
	private EditText id_editText;
	private EditText passwd_editText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		id_editText = (EditText) findViewById(R.id.userid_editText);
		passwd_editText = (EditText) findViewById(R.id.passwd_editText);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (GCMRegistrar.isRegistered(this)) {
			GCMRegistrar.unregister(this);
		}
	}

	public void loginOnClick(View v) {
		if (id_editText.getText().toString().equals("")) {
			Toast.makeText(this, "Input Id", Toast.LENGTH_SHORT).show();
			return;
		}
		if (passwd_editText.getText().toString().equals("")) {
			Toast.makeText(this, "Input password", Toast.LENGTH_SHORT).show();
		}

		submitLogin(id_editText.getText().toString(), passwd_editText.getText()
				.toString());
	}

	private void submitLogin(String userId, String password) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("userId", userId);
		params.put("password", password);
		final String paramStr = paramToString(params);

		new Thread() {
			@Override
			public void run() {
				URL url;
				HttpURLConnection conn = null;
				try {
					url = new URL(ServerConfig.SERVER_URL + "/user/login.do");
					conn = (HttpURLConnection) url.openConnection();
					conn.setConnectTimeout(ServerConfig.getConnTimeout() * 1000);
					conn.setReadTimeout(ServerConfig.getReadTimeout() * 1000);
					conn.setRequestMethod("POST");
					conn.setRequestProperty("Content-Type",
							"application/json;charset=UTF-8");
					conn.setDoOutput(true);
					conn.setDoInput(true);
					OutputStream os = conn.getOutputStream();
					os.write(paramStr.getBytes());
					os.flush();
					os.close();

					Message message = handler.obtainMessage();
					Bundle bundle = new Bundle();
					int responseCode = conn.getResponseCode();
					if (HttpURLConnection.HTTP_OK == responseCode) {
						BufferedReader br = new BufferedReader(
								new InputStreamReader(conn.getInputStream()));
						StringBuffer buf = new StringBuffer();
						while (true) {
							String str = br.readLine();
							if (null == str) {
								break;
							}
							buf.append(str);
						}

						bundle.putString("result", buf.toString());
						message.setData(bundle);
						handler.sendMessage(message);
					} else {
						Toast.makeText(MainActivity.this,
								conn.getResponseMessage(), Toast.LENGTH_SHORT)
								.show();
					}
					conn.disconnect();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					conn.disconnect();
				}
			}
		}.start();
	}

	protected String paramToString(Map<String, String> params) {
		StringBuilder bodyBuilder = new StringBuilder();
		Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
		// constructs the POST body using the parameters
		while (iterator.hasNext()) {
			Entry<String, String> param = iterator.next();
			bodyBuilder.append(param.getKey()).append('=')
					.append(param.getValue());
			if (iterator.hasNext()) {
				bodyBuilder.append('&');
			}
		}
		return bodyBuilder.toString();
	}

	@SuppressLint("HandlerLeak")
	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String json = msg.getData().getString("result");
			try {
				JSONObject object = new JSONObject(json)
						.getJSONObject("result");
				String result = object.getString("user");
				if (result.equals("1")) {
					Toast.makeText(MainActivity.this, "success",
							Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(MainActivity.this,
							TabActivity.class);

					SystemConfig.setUserId(id_editText.getText().toString());
					SystemConfig.setPassword(passwd_editText.getText()
							.toString());

					startActivity(intent);
					createGCM();
				} else {
					Toast.makeText(MainActivity.this, "fail",
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	};

	private void createGCM() {
		try {
			GCMRegistrar.checkDevice(this);
			GCMRegistrar.checkManifest(this);
		} catch (Exception e) {
			Log.e(TAG, "This device can't use GCM");
			return;
		}

		String regId = GCMRegistrar.getRegistrationId(this);
		if ("".equals(regId) || null == regId) {
			GCMRegistrar.register(this, ServerConfig.PROJECT_ID);
		} else {
			Log.d("", "Exist Registration Id:" + regId);
			SystemConfig.REGISTRATION_ID = regId;
		}
	}
}
