package com.cassandra.ch04


import java.nio.ByteBuffer

import org.apache.cassandra.thrift.Cassandra
import org.apache.cassandra.thrift.Column
import org.apache.cassandra.thrift.ColumnOrSuperColumn
import org.apache.cassandra.thrift.ColumnParent
import org.apache.cassandra.thrift.ColumnPath
import org.apache.cassandra.thrift.Mutation
import org.apache.cassandra.thrift.SuperColumn
import org.apache.log4j.Logger

import groovy.transform.TypeChecked

@TypeChecked
class PrePopulate {
	private Cassandra.Client client
	private static final Logger LOG = Logger.getLogger(PrePopulate.class)
	PrePopulate() throws Exception {
		client = new Connector().connect(Constants.KEYSPACE)
	}
	def void initialize() throws Exception {
		insertAllHotels()
		insertByCityIndexes()
		insertAllPointOfInterest()
	}
	private Column createColumn(byte[] name, byte[] value, Long timestamp) {
		def column = new Column()
		column.name = name
		column.value = value
		column.timestamp = timestamp
		return column
	}
	private void createCambriaHotel(String columnFamily, String rowKey, Long timestamp) {
		def parent = new ColumnParent(columnFamily)
		def key = ByteBuffer.wrap(rowKey.getBytes(Constants.UTF8))
		
		def nameCol = createColumn("name".getBytes(Constants.UTF8),
			"cambria suites hayden".getBytes(Constants.UTF8), timestamp)
		
		client.insert(key, parent, nameCol, Constants.CL)
	}
	private void insertPOIEmpireState() throws Exception {
		def outerMap = new HashMap<ByteBuffer, Map<String, List<Mutation>>>()
		def columnsToAdd = new ArrayList<Mutation>()
		
		def timestamp = System.currentTimeMillis()
		
		def esbName = "empire state building"
		def descCol = createColumn(str2Byte("desc"),
			str2Byte("great view"), timestamp)
		
		def esbCols = new ArrayList<Column>()
//		esbCols += descCol
		
		def innerMap = new HashMap<String, List<Mutation>>()
		def columns = new Mutation()
		def descCosc = new ColumnOrSuperColumn()
		def waldorfSC = new SuperColumn()
		waldorfSC.name = ByteBuffer.wrap(str2Byte(Constants.WALDORF_NAME)) 
		waldorfSC.columns = esbCols
		descCosc.super_column = waldorfSC
		columns.setColumn_or_supercolumn(descCosc)
		
		columnsToAdd += columns
		
		def superCFName = "pointofinterest"
		def cp = new ColumnPath()
		cp.column_family = superCFName
		cp.setSuper_column(str2Byte(Constants.WALDORF_NAME))
		cp.setSuper_columnIsSet(true)
	}
	private byte[] str2Byte(String str) {
		return str.getBytes(Constants.UTF8)
	}
	def void insertAllHotels() {
		
	}
	def void insertByCityIndexes() {
		
	}
	def void insertAllPointOfInterest() {
		
	}
}
