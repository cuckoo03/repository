package com.service.freetalkserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;


import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

public class Worker {
	private static final String APIKEY = "AIzaSyAK4MmYHi_wMeBjaTFtvgbtA6yi4nYp1Qs";
	private static final int RETRIES = 3;
	private static final String TASK_QUEUE_NAME = "gcm.queue";

	/**
	 * @param args
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws ConsumerCancelledException
	 * @throws ShutdownSignalException
	 */
	public static void main(String[] args) throws IOException,
			ShutdownSignalException, ConsumerCancelledException,
			InterruptedException {
		

		ApplicationContext context = new GenericXmlApplicationContext(
				"classpath:spring/application-context.xml");
		
		ConnectionFactory factory = context.getBean(ConnectionFactory.class);

		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		boolean durable = true;
		channel.queueDeclare(TASK_QUEUE_NAME, durable, false, false, null);
		System.out.println("waiting for messages.");

		channel.basicQos(1);

		QueueingConsumer consumer = new QueueingConsumer(channel);
		channel.basicConsume(TASK_QUEUE_NAME, false, consumer);

		while (true) {
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			String message = new String(delivery.getBody());

			System.out.println("Received " + message);
			receviceMessage(message);
			System.out.println("Done");

			channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
		}
	}

	private static void receviceMessage(String jsonMessage) {
		JSONObject json;
		try {
			json = new JSONObject(jsonMessage);
			String regId = json.getString("regId");
			String sendMessage = json.getString("sendMessage");

			Sender sender = new Sender(APIKEY);

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
