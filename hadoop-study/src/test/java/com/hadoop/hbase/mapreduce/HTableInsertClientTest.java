package com.hadoop.hbase.mapreduce;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.junit.Before;
import org.junit.Test;

public class HTableInsertClientTest {
	private MapDriver<LongWritable, Text, LongWritable, Text> mapDriver;

	@Before
	public void setUp() {
		mapDriver = new MapDriver<>(new HTableInsertClient.InsertMapper());
	}

	@Test
	public void testMapper() throws IOException {
		mapDriver.withInput(new LongWritable(1), new Text(""));
		mapDriver.withOutput(new LongWritable(1), new Text(""));
		mapDriver.runTest();
	}
}
