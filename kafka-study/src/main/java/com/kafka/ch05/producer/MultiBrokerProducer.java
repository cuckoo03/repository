package com.kafka.ch05.producer;

import java.util.Properties;
import java.util.Random;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import kafka.serializer.StringEncoder;

/**
 * 메시지 파티셔닝을 하는 자바 생산자 클라이언트
 * 
 * @author cuckoo03
 *
 */
public class MultiBrokerProducer {
	private static Producer<String, String> producer;
	private final Properties props = new Properties();

	public MultiBrokerProducer() {
		props.put("metadata.broker.list", "slave1:9092, slave1:9092");
		props.put("serializer.class", StringEncoder.class.getName());
		props.put("partitioner.class", SimplePartitioner.class.getName());
		props.put("request.required.acks", "2");
		ProducerConfig config = new ProducerConfig(props);
		producer = new Producer<>(config);
	}

	public static void main(String[] args) {
		MultiBrokerProducer sp = new MultiBrokerProducer();
		Random rnd = new Random();
		String topic = "multitopic";

		for (long messCount = 0; messCount < 10; messCount++) {
			Integer key = rnd.nextInt(10);
			String msg = "message:" + key;
			KeyedMessage<String, String> data = new KeyedMessage<>(topic,
					key.toString(), msg);
			producer.send(data);
		}
		producer.close();
	}
}