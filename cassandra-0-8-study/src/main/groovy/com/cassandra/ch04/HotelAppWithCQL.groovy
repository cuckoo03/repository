package com.cassandra.ch04

import java.nio.ByteBuffer

import org.apache.cassandra.thrift.Cassandra
import org.apache.cassandra.thrift.Column
import org.apache.cassandra.thrift.Compression

import groovy.transform.TypeChecked

@TypeChecked
class HotelAppWithCQL {
	private Cassandra.Client client
	def static String byteBufferToString(ByteBuffer buffer) throws Exception {
		def bytes = new byte[buffer.limit() - buffer.position()]
		buffer.get(bytes)
		return new String(bytes, Constants.UTF8)
	}
	def static byte[] stringToBytes(String str) {
		return str.getBytes(Constants.UTF8)
	}
	def static String bytesToString(byte[] bytes) {
		return new String(bytes, Constants.UTF8)
	}
	static void main(args) {
		new PrePopulate().initialize()
		println "database filled"	
		
		println "starting"
		def app = new HotelAppWithCQL()
		
		// find hotel by city
		def hotels = app.findHOtelByState("state1")
		println "found hotels in city. result:" + hotels.size()
		
		def h = hotels[0]
		println "hotel:$h.name"
		
		println "all done"
		
	}
	def List<Hotel> findHOtelByState(String state) throws Exception {
		println "searching for hotels:$state"
		
		client = new Connector().connect(Constants.KEYSPACE)
		
		def cql = "select name, city, state from Hotel where state='$state'"
		def result = client.execute_cql_query(
			ByteBuffer.wrap(cql.getBytes()), Compression.NONE)
		
		def columns = result.getRows()[0].getColumns()
		
		def results = new ArrayList<Hotel>()
		def hotel = new Hotel()
		
		def name = null
		def value = null
		columns.forEach({ Column col ->
			name = bytesToString(col.name)
			value = bytesToString(col.value)
			if (name == "name")
				hotel.name = value
			
			if (name == "city")
				hotel.city = value
				
			if (name == "state")
				hotel.state = value
				
			if (hotel.name != null && hotel.city != null && hotel.state != null) {
				results += hotel
				hotel = new Hotel()
			}
		})
		return results
	} 
}
