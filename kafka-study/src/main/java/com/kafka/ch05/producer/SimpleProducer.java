package com.kafka.ch05.producer;

import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

/**
 * 간단한 자바 생산자 클라이언트
 * @author cuckoo03
 *
 */
public class SimpleProducer {
	private static Producer<Integer, String> producer;
	private final Properties props = new Properties();

	public SimpleProducer() {
		props.put("metadata.broker.list", "master:9092");
		props.put("serializer.class", "kafka.serializer.StringEncoder");
		props.put("request.required.acks", "1");
		producer = new Producer<>(new ProducerConfig(props));
	}

	public static void main(String[] args) {
		SimpleProducer sp = new SimpleProducer();
		String topic = "multitopic";
		String messageStr = "hello everyone";
		KeyedMessage<Integer, String> data = new KeyedMessage<>(topic,
				messageStr);
		producer.send(data);
		producer.close();
	}
}