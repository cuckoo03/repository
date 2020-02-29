package com.kafka.onlybooks.ch04

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.KafkaConsumer

import groovy.transform.TypeChecked

@TypeChecked
class Consumer {
	static void main(args) {
		def props = new Properties();
		props.put(KAFKA_CONSTANTS.BOOTSTRAP_SERVERS, "220.230.113.172:9092");
		props.put(KAFKA_CONSTANTS.GROUP_ID, "kafka");
		props.put(KAFKA_CONSTANTS.ENABLE_AUTO_COMMIT, "true");
		props.put(KAFKA_CONSTANTS.AUTO_OFFSET_RESET, "latest");
		props.put(KAFKA_CONSTANTS.KEY_DESERIALIZER,
				KAFKA_CONSTANTS.STRING_DESERIALIZER);
		props.put(KAFKA_CONSTANTS.VALUE_DESERIALIZER,
				KAFKA_CONSTANTS.STRING_DESERIALIZER);
		
		def consumer = new KafkaConsumer<>(props);
		consumer.subscribe(Arrays.asList("mytopic4"));
		try {
			while (true) {
				def records = consumer.poll(100);
				for (ConsumerRecord<String, String> record : records)
					printf("Topic: %s, Partition: %s, Offset: %d, Key: %s, Value: %s\n", record.topic(), record.partition(), record.offset(), record.key(), record.value());
			}
		} finally {
			consumer.close();
		}
	}
}
