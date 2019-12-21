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
		
		def nameCol = createColumn(
			stringToBytes("name"), stringToBytes("name1"), timestamp)
		def phoneCol = createColumn(
			stringToBytes("phone"), stringToBytes("phone1"), timestamp)
		def cityCol = createColumn(
			stringToBytes("city"), stringToBytes("city1"), timestamp)
		def stateCol = createColumn(
			stringToBytes("state"), stringToBytes("state1"), timestamp)
		def zipCol = createColumn(
			stringToBytes("zip"), stringToBytes("zip1"), timestamp)
		
		client.insert(key, parent, nameCol, Constants.CL)
		client.insert(key, parent, phoneCol, Constants.CL)
		client.insert(key, parent, cityCol, Constants.CL)
		client.insert(key, parent, stateCol, Constants.CL)
		client.insert(key, parent, zipCol, Constants.CL)
		
	}
	private void insertPOIEmpireState() throws Exception {
		def outerMap = new HashMap<ByteBuffer, Map<String, List<Mutation>>>()
		def columnsToAdd = new ArrayList<Mutation>()
		
		def timestamp = System.currentTimeMillis()
		
		def esbName = "rowkey1"
		def col1 = createColumn(stringToBytes("col1"),
			stringToBytes("col1Val"), timestamp)
		def col2 = createColumn(stringToBytes("col2"),
			stringToBytes("col2Val"), timestamp)
		
		def esbCols = new ArrayList<Column>()
		esbCols += col1
		esbCols += col2
		
		def innerMap = new HashMap<String, List<Mutation>>()
		def columns = new Mutation()
		def descCosc = new ColumnOrSuperColumn()
		def superCol1 = new SuperColumn()
		superCol1.name = ByteBuffer.wrap(stringToBytes(Constants.SC1_NAME)) 
		superCol1.columns = esbCols
		descCosc.super_column = superCol1
		columns.setColumn_or_supercolumn(descCosc)
		
		columnsToAdd += columns
		
		def superCFName = "PointOfInterest"
		def cp = new ColumnPath()
		cp.column_family = superCFName
		cp.setSuper_column(stringToBytes(Constants.SC1_NAME))
		cp.setSuper_columnIsSet(true)
		
		innerMap[superCFName] = columnsToAdd
		outerMap[ByteBuffer.wrap(stringToBytes(esbName))] = innerMap
		
		client.batch_mutate(outerMap, Constants.CL)
		
		println("done inserting poi1")
	}
	private byte[] stringToBytes(String str) {
		return str.getBytes(Constants.UTF8)
	}
	def void insertAllHotels() {
		def columnFamily = "Hotel"
		def cambriaKey = "row1"
		def timestamp = System.currentTimeMillis()
		
		createCambriaHotel(columnFamily, cambriaKey, timestamp)
		println("inserted $cambriaKey")
	}
	def void insertByCityIndexes() {
		
	}
	def void insertAllPointOfInterest() {
		println("inserting pois")
		insertPOIEmpireState()
		println("done inserting pois")
	}
}
