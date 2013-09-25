package com.service.freetalk.service;

import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.android.gcm.server.Sender;
import com.service.freetalk.biz.UserBiz;

public class UserService {
	private static final String APIKEY = "AIzaSyAK4MmYHi_wMeBjaTFtvgbtA6yi4nYp1Qs";
	private static final int RETRIES = 3;
	@Autowired
	private UserBiz userBiz;

	@Autowired
	private AmqpTemplate amqpTemplate;

	/*
	public void setUserBiz(UserBiz userBiz) {
		this.userBiz = userBiz;
	}
	
	public void setAmqpTemplate(AmqpTemplate amqpTemplate) {
		this.amqpTemplate = amqpTemplate;
	}
	*/

	public String loginUser(Map<String, String> param) {
		return userBiz.loginUser(param);
	}

	public List findUserList(String userId) {
		return userBiz.findUserList(userId);
	}

	public int registerId(String regId, String userId) {
		return userBiz.registerId(regId, userId);
	}

	public void sendMessage(String body) {
		try {
			JSONObject json = new JSONObject(body);
			String receiverId = userBiz.findRegId(json.getString("receiverId"));
			String sendMessage = json.getString("sendMessage");

			Sender sender = new Sender(APIKEY);

			publishGCMMessage(receiverId, sendMessage);
			
			/*
			Message message = new Message.Builder().addData("title", "title")
					.addData("msg", sendMessage).build();

			List<String> list = new ArrayList<String>();
			list.add(regId);
		
			MulticastResult multiResult = sender.send(message, list, RETRIES);
			if (multiResult != null) {
				List<Result> resultList = multiResult.getResults();
				for (Result result : resultList) {
					if (null != result.getMessageId()) {
						String canonicalRegId = result
								.getCanonicalRegistrationId();
						if (null != canonicalRegId) {
							// same device has more than on registration id :
							// update
							// database
						} else {
							System.out.println(result.getMessageId());
						}
					} else {
						String error = result.getErrorCodeName();
						if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
							// application has been removed from device :
							// unregister database
							userBiz.unregisterID(regId);
							System.out.println(Constants.ERROR_NOT_REGISTERED);
						}
					}
				}
			}
			*/
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void publishGCMMessage(String regId, String sendMessage)
			throws JSONException {
		JSONObject json = new JSONObject();
		json.put("regId", regId);
		json.put("sendMessage", sendMessage);
		
		amqpTemplate.convertAndSend("gcm.queue", json.toString());
	}
}