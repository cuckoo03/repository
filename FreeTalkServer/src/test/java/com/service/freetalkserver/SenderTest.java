package com.service.freetalkserver;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class SenderTest {
	public void send(String url, String queueName, String message, int maxThread)
			throws IOException {
		ApplicationContext context = new GenericXmlApplicationContext(
				"classpath:spring/application-context.xml");

		ConnectionFactory factory = context.getBean(ConnectionFactory.class);
		ExecutorService executorService = Executors
				.newFixedThreadPool(maxThread);
		Connection conn = factory.newConnection(executorService);
		Channel channel = null;
		byte[] msg = null;
		try {
			for (int i = 0; i < maxThread; i++) {
				msg = (message + i).getBytes();
				channel = (Channel) conn.createChannel();
				channel.basicPublish("", queueName, null, msg);
				System.out.println("send message:" + msg);
				channel.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}

		System.out.println("messages send end");
		System.exit(0);
	}

	public static void main(String[] args) {
		SenderTest sender = new SenderTest();
		try {
			sender.send("192.168.0.1", "gcm.queue", "message", 30);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
