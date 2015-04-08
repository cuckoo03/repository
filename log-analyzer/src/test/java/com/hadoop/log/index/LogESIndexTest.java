package com.hadoop.log.index;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.junit.Before;
import org.junit.Test;

public class LogESIndexTest {
	private MapDriver<LongWritable, Text, LongWritable, Text> mapDriver;

	@Before
	public void setUp() {
		mapDriver = new MapDriver<>(new LogESIndex.IndexMapper());
	}

	@Test
	public void mapperTest() throws IOException {
		String input = "127.0.0.1 - - [20/Aug/2014:10:26:05 +0900] \"GET / HTTP/1.1\" 404 951";
		mapDriver.addInput(new LongWritable(1), new Text(input));
		mapDriver.addOutput(new LongWritable(1), new Text(input));
		mapDriver.runTest();
	}

	@Test
	public void createJsonObjectTest() {

	}
}
