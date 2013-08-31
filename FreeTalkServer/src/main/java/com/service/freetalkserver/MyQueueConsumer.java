package com.service.freetalkserver;

import java.io.IOException;
import java.util.UUID;
import java.util.logging.Logger;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class MyQueueConsumer extends DefaultConsumer {
	Logger log = Logger.getLogger(MyQueueConsumer.class.getName());
	Channel channel;

	public MyQueueConsumer(Channel channel) {
		super(channel);
		this.channel = channel;
	}

	@Override
	public void handleDelivery(String consumerTag, Envelope envelope,
			AMQP.BasicProperties properties, byte[] body) throws IOException {
		String routingKey = envelope.getRoutingKey();
		String contentType = properties.getContentType();
		long deliveryTag = envelope.getDeliveryTag();

		String msg = new String(body);
		UUID uuid = UUID.randomUUID();
		log.info(uuid + "'s channel:" + channel + ", Thread:"
				+ Thread.currentThread() + " msg:" + msg);

		this.channel.basicAck(deliveryTag, false);
	}
}
