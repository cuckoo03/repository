package com.cassandra.ch04


import org.apache.cassandra.thrift.ConsistencyLevel

import groovy.transform.Immutable
import groovy.transform.ToString
import groovy.transform.TypeChecked

@TypeChecked
class Constants {
	def static final String SC1_NAME = "superCol1"
	def static final String UTF8 = "UTF8"
	def static final String KEYSPACE = "Hotelier"
	def static final ConsistencyLevel CL = ConsistencyLevel.ONE
	def static final String HOST = "localhost"
	def static final int PORT = 9160	
}
