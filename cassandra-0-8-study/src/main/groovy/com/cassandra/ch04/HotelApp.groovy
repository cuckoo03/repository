package com.cassandra.ch04

import java.nio.ByteBuffer

import org.apache.cassandra.thrift.Cassandra
import org.apache.cassandra.thrift.ColumnParent
import org.apache.cassandra.thrift.IndexClause
import org.apache.cassandra.thrift.IndexExpression
import org.apache.cassandra.thrift.IndexOperator
import org.apache.cassandra.thrift.KeyRange
import org.apache.cassandra.thrift.KeySlice
import org.apache.cassandra.thrift.SlicePredicate
import org.apache.cassandra.thrift.SliceRange
import org.apache.log4j.Logger

import groovy.transform.TypeChecked

@TypeChecked
class HotelApp {
	private Cassandra.Client client
	def static String byteBufferToStr(ByteBuffer buffer) throws Exception {
		def bytes = new byte[buffer.limit() - buffer.position()]
		buffer.get(bytes)
		return new String(bytes, Constants.UTF8)
	}
	def static byte[] strToBytes(String str) {
		return str.getBytes(Constants.UTF8)
	}
	def static String bytesToString(byte[] bytes) {
		return new String(bytes, Constants.UTF8)
	} 
	static void main(args) throws Exception {
		new PrePopulate().initialize()
		println("database filled")
		
		def app = new HotelApp()
		
		def hotels = app.findHotelByStateCity("state1")
		
		println("found hotels in city. results:${hotels.size()}")
		
		def h = hotels[0]
		println("pick hotel name:$h.name")
		
		println("finding points of interest near $h.name")
		def points = app.findPOIByHotel("superCol1")
		
		if (points.size() > 0) {
			def poi = points[0]
			println("found poi. $poi.name, $poi.val")
		} else {
			println "points size is zero"
		}
		
		println("all done")
	}
	//컬럼 슬라이스를 사용해 슈퍼 컬럼에서 데이터를 가져온다
	def List<POI> findPOIByHotel(String hotel) throws Exception {
		// 쿼리
		def sliceRange = new SliceRange()
		sliceRange.start = ByteBuffer.wrap(strToBytes(hotel))
		sliceRange.finish = ByteBuffer.wrap(strToBytes(hotel))
		sliceRange.reversed = false
		
		def slicePredicate = new SlicePredicate()
		slicePredicate.slice_range = sliceRange
		
		// 로우에 있는 모든 컬럼을 읽어온다
		def scFamily = "PointOfInterest"
		def parent = new ColumnParent(scFamily)
		
		def keyRange = new KeyRange()
		keyRange.start_key = ByteBuffer.allocate(0)
		keyRange.end_key = ByteBuffer.allocate(0)
		
		def pois = new ArrayList<POI>()
		// 간단한 리스트가 아니라 키와 로우 키를 매핑한다
		// 로우 키 + 첫 번째 컬럼으로만 인덱스를 생성한다
		def slices = client.get_range_slices(
			parent, slicePredicate, keyRange, Constants.CL)
		slices.forEach({ KeySlice slice ->
			def cols = slice.columns
			
			def poi = new POI()
			poi.name = bytesToString(slice.key)
			for (def cosc : cols) {
				def sc = cosc.super_column
				def colsInSc = sc.columns
				def colName = null
				for (def c : colsInSc) {
					colName = bytesToString(c.name)
					if (colName == "col1")
						poi.name = bytesToString(c.value)
					
					if (colName == "col2")
						poi.val = bytesToString(c.value)
				}
				println("poi column. col1 val:$poi.name, col2 val:$poi.val")
				pois += poi
			}
		}) 
		
		return pois
	}
	// 키 레인지를 사용한다
	def List<Hotel> findHotelByStateCity(String state) throws Exception {
		println("searching for hotels in $state")
		
		// 쿼리
		def indexClause = new IndexClause()
		indexClause.start_key = ByteBuffer.allocate(0)
		def indexExp = new IndexExpression()
		indexExp.column_name = ByteBuffer.wrap(strToBytes("state"))
		indexExp.value = ByteBuffer.wrap(strToBytes(state))
		indexExp.op = IndexOperator.EQ
		
		indexClause.addToExpressions(indexExp)
		
		def sliceRange = new SliceRange()
		sliceRange.start = ByteBuffer.allocate(0)
		sliceRange.finish = ByteBuffer.allocate(0)
		sliceRange.reversed = false
		
		def slicePredicate = new SlicePredicate()
		slicePredicate.slice_range = sliceRange
		
		// 로우의 코든 컬럼을 읽어온다
		def columnFamily = "Hotel"
		def columnParent = new ColumnParent(columnFamily)
		
		client = new Connector().connect(Constants.KEYSPACE)
		def keys = client.get_indexed_slices(
			columnParent, indexClause, slicePredicate, Constants.CL)
		def results = new ArrayList<Hotel>()
		keys.forEach({ KeySlice slice ->
			def coscs = slice.columns
			println("using key:" + bytesToString(slice.key))
			
			def hotel = new Hotel()
			def name = null
			def value = null
			for (def cs : coscs) {
				name = bytesToString(cs.column.name)
				value = bytesToString(cs.column.value)
				
				if (name == "name")
					hotel.name = value
					
				if (name == "city")
					hotel.city = value
					
				if (name =="state")
					hotel.state = value
				
				if (hotel.name != null && hotel.city != null && hotel.state != null) {
					results += hotel
					hotel = new Hotel()
				}
			}
		})
		return results
	}
}
