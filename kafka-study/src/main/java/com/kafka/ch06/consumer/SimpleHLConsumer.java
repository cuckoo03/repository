package com.kafka.ch06.consumer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

/**
 * 단순 상위 레벨 소비자 클라이언트
 * 
 * @author cuckoo03
 *
 */
public class SimpleHLConsumer {
	private final ConsumerConnector consumer;
	private final String topic;

	public SimpleHLConsumer(String zookeeper, String groupId, String topic) {
		Properties props = new Properties();
		props.put("zookeeper.connect", zookeeper);
		props.put("group.id", groupId);
		props.put("zookeeper.session.timeout.ms", "500");
		props.put("zookeeper.sync.time.ms", "250");
		props.put("auto.commit.interval.ms", "1000");

		consumer = Consumer.createJavaConsumerConnector(new ConsumerConfig(
				props));
		this.topic = topic;
	}

	public void testConsumer() {
		Map<String, Integer> topicCount = new HashMap<>();
		topicCount.put(topic, new Integer(1));
		Map<String, List<KafkaStream<byte[], byte[]>>> consumerStreams = consumer
				.createMessageStreams(topicCount);
		List<KafkaStream<byte[], byte[]>> streams = consumerStreams.get(topic);

		for (final KafkaStream<byte[], byte[]> stream : streams) {
			ConsumerIterator<byte[], byte[]> consumerIter = stream.iterator();
			while (consumerIter.hasNext()) {
				System.out.println("message from single topic:"
						+ new String(consumerIter.next().message()));
			}
		}
		if (consumer != null) {
			consumer.shutdown();
		}
	}

	public static void main(String[] args) {
		SimpleHLConsumer consumer = new SimpleHLConsumer("master:2181",
				"testgroup", "kafkatopic");
		consumer.testConsumer();
	}
}