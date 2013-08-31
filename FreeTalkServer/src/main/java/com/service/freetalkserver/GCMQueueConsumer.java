package com.service.freetalkserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class GCMQueueConsumer extends DefaultConsumer {
	Logger log = Logger.getLogger(MyQueueConsumer.class.getName());
	Channel channel;
	private Sender sender;
	private final int retries;

	public GCMQueueConsumer(Channel channel, Sender sender, int retires) {
		super(channel);
		this.channel = channel;
		this.sender = sender;
		this.retries = retires;
	}

	@Override
	public void handleDelivery(String consumerTag, Envelope envelope,
			AMQP.BasicProperties properties, byte[] body) throws IOException {
		long deliveryTag = envelope.getDeliveryTag();

		String message = new String(body);
		UUID uuid = UUID.randomUUID();
		log.info(uuid + "'s channel:" + channel + ", Thread:"
				+ Thread.currentThread() + " msg:" + message);

		System.out.println("Received " + message);
		receviceMessage(message);

		this.channel.basicAck(deliveryTag, false);
	}

	private void receviceMessage(String jsonMessage) {
		JSONObject json;
		try {
			json = new JSONObject(jsonMessage);
			String regId = json.getString("regId");
			String sendMessage = json.getString("sendMessage");

			Message message = new Message.Builder().addData("title", "title")
					.addData("msg", sendMessage).build();

			List<String> list = new ArrayList<String>();
			list.add(regId);

			MulticastResult multiResult = sender.send(message, list, retries);
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
							System.out.println(Constants.ERROR_NOT_REGISTERED);
						}
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
