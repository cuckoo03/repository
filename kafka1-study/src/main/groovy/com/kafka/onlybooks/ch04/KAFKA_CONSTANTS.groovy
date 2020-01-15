package com.kafka.onlybooks.ch04

import groovy.transform.TypeChecked

@TypeChecked
class KAFKA_CONSTANTS {
	def static final BOOTSTRAP_SERVERS = "bootstrap.servers"
	def static final KEY_SERIALIZER = "key.serializer"
	def static final KEY_DESERIALIZER = "key.deserializer"
	def static final VALUE_SERIALIZER = "value.serializer"
	def static final VALUE_DESERIALIZER = "value.deserializer"
	def static final STRING_SERIALIZER = "org.apache.kafka.common.serialization.StringSerializer"
	def static final STRING_DESERIALIZER = "org.apache.kafka.common.serialization.StringDeserializer"
	def static final GROUP_ID = "group.id"
	/**
	 * true
	 */
	def static final ENABLE_AUTO_COMMIT = "enable.auto.commit"
	/**
	 * latest
	 */
	def static final AUTO_OFFSET_RESET = "auto.offset.reset"
	/**
	 * 0:프로튜서는 서버로부터 ack에 대한 응답을 기다리지 않음
	 * 1:리더는 데이터를 기록하지만 모든 팔로워는 확인하지 않음
	 * all or -1:리더는 ISR의 팔로워로부터 데이터에 대한 ack를 기다림
	 */
	def static final ACKS = "ACKS"
	/**
	 * available format:none, gzip, snappy, lz4
	 */
	def static final COMPRESSION_TYPE = "compression.type"
	def static final RETRIES = "retries"
	def static final BATCH_SIZE = "batch.size"
	def static final LINGER_MS = "linger.ms"
	def static final MAX_REQUEST_SIZE = "max.request.size"
}
