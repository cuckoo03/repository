package com.apps.freetalk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.freetalk.config.ServerConfig;
import com.apps.freetalk.config.SystemConfig;

public class TabActivity extends Activity {
	protected static final int CONN_TIMEOUT = 30;
	protected static final int READ_TIMEOUT = 30;

	private List<Map<String, String>> userList;
	private ListView listView;
	private ArrayAdapter<String> adapter;
	private TabHost tabHost;
	private TextView messageTextView;
	private EditText sendEditText;
	private Button sendButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab);

		tabHost = (TabHost) findViewById(R.id.tabHost);
		tabHost.setup();

		TabHost.TabSpec spec = tabHost.newTabSpec("tag1");
		spec.setContent(R.id.tab1);
		spec.setIndicator("유저");
		tabHost.addTab(spec);

		spec = tabHost.newTabSpec("tag2");
		spec.setContent(R.id.tab2);
		spec.setIndicator("채팅");
		tabHost.addTab(spec);

		tabHost.setCurrentTab(0);

		listView = (ListView) findViewById(R.id.tab1);
		listView.setOnItemClickListener(itemClickListener);

		messageTextView = (TextView) findViewById(R.id.messageTextView);
		sendEditText = (EditText) findViewById(R.id.sendEditText);
		sendButton = (Button) findViewById(R.id.sendButton);

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				findUserList();
			}
		});
		t.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tab, menu);
		return true;
	}

	public void sendButtonOnClick(View view) {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				sendMessage();
			}
		});
		t.start();
	}

	private void sendMessage(){
		URL url;
		HttpURLConnection conn = null;
		try {
			JSONObject json = new JSONObject();
			json.put("receiverId", selectedId);
			json.put("sendMessage", sendEditText.getText().toString());

			url = new URL(ServerConfig.SERVER_URL + "/device/send.do");
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(CONN_TIMEOUT * 1000);
			conn.setReadTimeout(READ_TIMEOUT * 1000);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type",
					"application/json;charset=UTF-8");
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
				
				Message message = sendMessageHandler.obtainMessage();
				Bundle bundle = new Bundle();
				
				bundle.putString("result", "");
				message.setData(bundle);
				sendMessageHandler.sendMessage(message);
			} else {
				Toast.makeText(this, "Send Failed.", Toast.LENGTH_SHORT).show();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	private void findUserList() {
		URL url;
		HttpURLConnection conn = null;
		try {
			JSONObject json = new JSONObject();
			json.put("userId", SystemConfig.getUserId());

			url = new URL(ServerConfig.SERVER_URL + "/user/userList.do");
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(CONN_TIMEOUT * 1000);
			conn.setReadTimeout(READ_TIMEOUT * 1000);
			conn.setRequestMethod("POST");
			// content-type을 application/x-www-form-urlencoded 으로 할경우
			// 서버 컨트롤러에서 해당 값이 인코딩 되어 전송되는 문제로 인해 기본 값을 변경함
			// spring 3.0.7부터 발생
			conn.setRequestProperty("Content-Type",
					"application/json;charset=UTF-8");
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


					Message message = findUserListHandler.obtainMessage();
					Bundle bundle = new Bundle();
					
					bundle.putString("result", buf.toString());
					message.setData(bundle);
					findUserListHandler.sendMessage(message);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private String selectedId;

	private OnItemClickListener itemClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> adapterView, View clickedView,
				int pos, long id) {
			selectedId = ((TextView) clickedView).getText().toString();

			tabHost.setCurrentTab(1);
		}
	};
	
	@SuppressLint("HandlerLeak") 
	private final Handler findUserListHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String json = msg.getData().getString("result");
			
			String listJson;
			try {
				listJson = new JSONObject(json).getString("result");
				ObjectMapper mapper = new ObjectMapper();
				userList = mapper.readValue(listJson.toString(),
						new TypeReference<List<Map<String, String>>>() {
				});
				
				List<String> adapterList = new ArrayList<String>();
				
				for (Map<String, String> item : userList) {
					adapterList.add(item.get("ID"));
				}
				adapter = new ArrayAdapter<String>(TabActivity.this,
						android.R.layout.simple_list_item_1, adapterList);
				listView.setAdapter(adapter);
			} catch (JSONException e) {
				e.printStackTrace();
				Log.e("error", "json exception");
				return;
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};
	
	private final Handler sendMessageHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			messageTextView.append(SystemConfig.getUserId() + ":"
					+ sendEditText.getText().toString() + "\n");
			sendEditText.setText("");
		}
	};
}
