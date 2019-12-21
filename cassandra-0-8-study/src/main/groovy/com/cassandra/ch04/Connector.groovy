package com.cassandra.ch04

import org.apache.cassandra.thrift.Cassandra
import org.apache.cassandra.thrift.InvalidRequestException
import org.apache.cassandra.thrift.TBinaryProtocol
import org.apache.thrift.TException
import org.apache.thrift.transport.TFramedTransport
import org.apache.thrift.transport.TSocket
import org.apache.thrift.transport.TTransport
import org.apache.thrift.transport.TTransportException

import groovy.transform.TypeChecked

@TypeChecked
class Connector {
	private static final String HOST = "220.230.124.211"
	private TTransport tr = new TSocket(HOST, 9160)

	def Cassandra.Client connect() throws TTransportException, TException,
	InvalidRequestException {
		def tf = new TFramedTransport(tr)
		def proto = new TBinaryProtocol(tf)
		def client = new Cassandra.Client(proto)
		tr.open()
		return client
	}
	def Cassandra.Client connect(String keyspace) throws TTransportException,
	TException, InvalidRequestException {
		def tf = new TFramedTransport(tr)
		def proto = new TBinaryProtocol(tf)
		def client = new Cassandra.Client(proto)
		tr.open()
		client.set_keyspace(keyspace)
		return client
	}
	def void close() {
		tr.close()
	}
}
