package com.cassandra.ch04


import org.apache.cassandra.thrift.ConsistencyLevel

import groovy.transform.Immutable
import groovy.transform.ToString
import groovy.transform.TypeChecked

@TypeChecked
@ToString
@Immutable
class Constants {
	def static final String CAMBRIA_NAME = "cambria suites hayden"
	def static final String CLARION_NAME = "clarion scottsdale peek"
	def static final String W_NAME = "the w sf"
	def static final String WALDORF_NAME = "the waldorf=astoria"
	def static final String UTF8 = "UTF8"
	def static final String KEYSPACE = "Hotelier"
	def static final ConsistencyLevel CL = ConsistencyLevel.ONE
	def static final String HOST = "localhost"
	def static final int PORT = 9160	
}
