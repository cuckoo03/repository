package com.apps.freetalk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import com.apps.freetalk.config.HttpConstant;
import com.apps.freetalk.config.ServerConfig;
import com.apps.freetalk.config.SystemConfig;
import com.google.android.gcm.GCMBaseIntentService;

/**
 * 대부분의 예제가 해당 클래스를 루트 패키지 내에 있어야 한다고 했지만 현재는 루트패키지에 놓지 않아도 문제가 없는거 같음
 * 
 * @author cuckoo03
 * 
 */
public class GCMIntentService extends GCMBaseIntentService {
	/**
	 * GCM Server로부터 발급받은 ProjectID를 통해 superclass를 생성
	 * 
	 */
	public GCMIntentService() {
		super(ServerConfig.PROJECT_ID);
	}

	private static final String REGISTER_ID = "/device/register.do";

	/**
	 * GCM 오류 발생시 처리해야 할 코드 GCM 홈페이지와 GCMConstants 내 상수를 참조
	 */
	@Override
	protected void onError(Context arg0, String arg1) {
		Log.d("", "onError");
	}

	/**
	 * GCMserver가 전송하는 메시지가 전상 처리된 경우
	 */
	@Override
	protected void onMessage(Context context, Intent intent) {
		String msg = intent.getStringExtra("msg");
		Log.e("getmessage", "getmessage:" + msg);

		generateNotification(context, msg);
	}

	/**
	 * GCMRegistrar.getRegisterationId(contenxt)가 실행되어 gcm server에서
	 * registrationID를 발급받은 경우 메서드가 콜백됨 사용자 계정 db에 디바이스id를 등록한다
	 */
	@Override
	protected void onRegistered(Context context, String regId) {
		Log.e("register key", regId);
		
		URL url;
		HttpURLConnection conn = null;
		try {
			url = new URL(ServerConfig.SERVER_URL + REGISTER_ID);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty(HttpConstant.CONTENT_TYPE,
					"application/json;charset=UTF-8");
			conn.setDoInput(true);
			conn.setDoOutput(true);

			OutputStream os = conn.getOutputStream();
			JSONObject json = new JSONObject();
			json.put("regId", regId);
			json.put("userId", SystemConfig.getUserId());
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

				SystemConfig.REGISTRATION_ID = regId;
			} else {
				Toast.makeText(this, conn.getResponseMessage(),
						Toast.LENGTH_SHORT).show();
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

	/**
	 * GCMRegistrar.unregister(contxt) 호출로 해당 디바이스의 registrationID를 해지 요청한다.
	 */
	@Override
	protected void onUnregistered(Context arg0, String regId) {
		Log.e("unregister device key", regId);
	}

	private void generateNotification(Context context, String message) {
		int icon = com.apps.freetalk.R.drawable.ic_launcher;
		long when = System.currentTimeMillis();

		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		@SuppressWarnings("deprecation")
		Notification notification = new Notification(icon, message, when);

		String title = context.getString(R.string.app_name);
		Intent notificationIntent = new Intent(context, TabActivity.class);

		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent intent = PendingIntent.getActivity(context, 0,
				notificationIntent, 0);

		Vibrator vibrator = (Vibrator) context
				.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(1000);

		notification.setLatestEventInfo(context, title, message, intent);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notificationManager.notify(0, notification);
	}
}
