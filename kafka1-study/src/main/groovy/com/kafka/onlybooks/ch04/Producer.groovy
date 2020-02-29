package com.kafka.onlybooks.ch04

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord

import groovy.transform.TypeChecked

@TypeChecked
class Producer {
	static void main(args) {
		def props = new Properties()
		props.put(KAFKA_CONSTANTS.BOOTSTRAP_SERVERS, "220.230.113.172:9092")
		props.put(KAFKA_CONSTANTS.KEY_SERIALIZER, 
			KAFKA_CONSTANTS.STRING_SERIALIZER)
		props.put(KAFKA_CONSTANTS.VALUE_SERIALIZER, 
			KAFKA_CONSTANTS.STRING_SERIALIZER)
		props.put(KAFKA_CONSTANTS.ACKS, 1)
		props.put(KAFKA_CONSTANTS.COMPRESSION_TYPE, "gzip")
		
		def producer = new KafkaProducer<String, String>(props)
		
		// sync style
		/*
		try {
			def metadata = producer.send(
			new ProducerRecord<String, String>("mytopic3", "send")).get()
			println "partition:${metadata.partition()}, offset:${metadata.offset()}"
		} catch (Exception e) {
			e.printStackTrace()
		} finally {
			producer.close()
		}
		*/
		
		// async style
		try {
			producer.send(new ProducerRecord<String, String>("mytopic4", "send"),
					new AsyncCallback())
		} catch (Exception e) {
			e.printStackTrace()
		} finally {
			producer.close()
		}
	}
}
