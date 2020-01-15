package com.kafka.onlybooks.ch04

import org.apache.kafka.clients.producer.Callback
import org.apache.kafka.clients.producer.RecordMetadata

import groovy.transform.TypeChecked

@TypeChecked
class AsyncCallback implements Callback {
	@Override
	def void onCompletion(RecordMetadata metadata, Exception exception) {
		if (metadata != null)
			println "partition:${metadata.partition()}, offset:${metadata.offset()}"
		else
			exception.printStackTrace()
	}
}
