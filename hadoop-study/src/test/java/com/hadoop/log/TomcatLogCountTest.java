package com.hadoop.log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.junit.Before;
import org.junit.Test;

public class TomcatLogCountTest {
	private MapDriver<LongWritable, Text, Text, LogTuple> mapDriver;
	private ReduceDriver<Text, LogTuple, Text, LongWritable> reduceDriver;

	@Before
	public void setUp() {
		mapDriver = new MapDriver<>(new TomcatLogCount.LogCountMapper());
		reduceDriver = new ReduceDriver<>(new TomcatLogCount.LogCountReducer());
	}

	@Test
	public void testMapper() throws IOException {
		String str = "0:0:0:0:0:0:0:1 - - [30/Jul/2014:17:34:16 +0900] GET /goodnight/common/ver_dual HTTP/1.1 200 223";

		mapDriver.withInput(new LongWritable(1), new Text(str));
		mapDriver.withOutput(new Text("1"), new LogTuple());
		mapDriver.runTest();
	}

	@Test
	public void testReducer() throws IOException {
		List<LogTuple> values = new ArrayList<>();
		LogTuple tuple = new LogTuple();
		tuple.setLocalhost("localhost1");
		values.add(tuple);
		tuple.setLocalhost("localhost2");
		values.add(tuple);
		reduceDriver.withInput(new Text("1"), values);
		reduceDriver.withOutput(new Text("12"), new LongWritable(1));
		reduceDriver.runTest();
	}
}
