package com.cassandra.ch06


import org.apache.cassandra.thrift.Cassandra
import org.apache.cassandra.thrift.CfDef
import org.apache.cassandra.thrift.KsDef
import org.apache.cassandra.thrift.TBinaryProtocol
import org.apache.thrift.transport.TFramedTransport
import org.apache.thrift.transport.TSocket

import groovy.transform.TypeChecked

@TypeChecked
class ConfigAPI {
	private static final String HOST = "cassandra.ip"
	private static final int PORT = 9160
	private static String STRATEGY_CLASS =
	"org.apache.cassandra.locator.OldNetworkTopologyStrategy"
	static void main(args) {
		println ""
		def keyspaceName = "dynamicKeyspace"

		// create keyspace
		def k = new KsDef()
		k.setName(keyspaceName)
		k.setReplication_factorIsSet(true)
		k.setReplication_factor(1)

		k.setStrategy_class(STRATEGY_CLASS)
		def cfDefs = new ArrayList<CfDef>()
		k.setCf_defs(cfDefs)
		
		// connect server
		def tr = new TSocket(HOST, PORT)
		def tf = new TFramedTransport(tr)
		def proto = new TBinaryProtocol(tf)
		def client = new Cassandra.Client(proto)
		tr.open()
		
		// add new keyspace
		client.system_add_keyspace(k)
		println "added keyspace:$keyspaceName"
	}
}
