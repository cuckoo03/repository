package com.kafka.ch06.consumer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

/**
 * 다중 파티션 토픽 멀티스레드 소비자 클라이언트
 * 
 * @author cuckoo03
 *
 */
public class MultithreadHLConsumer {
	private ExecutorService executor;
	private final ConsumerConnector consumer;
	private final String topic;

	public MultithreadHLConsumer(String zookeeper, String groupId, String topic) {
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

	public void testConsumer(int threadCount) {
		Map<String, Integer> topicCount = new HashMap<>();
		topicCount.put(topic, new Integer(threadCount));

		Map<String, List<KafkaStream<byte[], byte[]>>> consumerStreams = consumer
				.createMessageStreams(topicCount);
		List<KafkaStream<byte[], byte[]>> streams = consumerStreams.get(topic);

		executor = Executors.newFixedThreadPool(threadCount);
		int threadNumber = 0;
		for (final KafkaStream<byte[], byte[]> stream : streams) {
			threadNumber++;
			executor.execute(new ConsumerThread(stream, threadNumber));
		}

		try {
			Thread.sleep(100000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if (consumer != null) {
			consumer.shutdown();
		}
		if (executor != null) {
			executor.shutdown();
		}
	}

	public static void main(String[] args) {
		String topic = "threadtopic";
		int threadCount = 4;
		MultithreadHLConsumer consumer = new MultithreadHLConsumer(
				"master:2181", "testgroup", topic);
		consumer.testConsumer(threadCount);
	}
}

class ConsumerThread implements Runnable {
	private KafkaStream<byte[], byte[]> stream;
	private int threadNumber;

	public ConsumerThread(KafkaStream<byte[], byte[]> stream, int threadNumber) {
		this.stream = stream;
		this.threadNumber = threadNumber;
	}

	@Override
	public void run() {
		ConsumerIterator<byte[], byte[]> consumerIter = stream.iterator();
		while (consumerIter.hasNext()) {
			System.out.println("Message from thread:" + threadNumber + "-"
					+ new String(consumerIter.next().message()));
			System.out.println("Shutting down thread:" + threadNumber);
		}
	}
}