package com.hadoop.log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.junit.Before;
import org.junit.Test;

public class TomcatLogCountTest {
	private MapDriver<LongWritable, Text, LongWritable, LogTuple> mapDriver;
	private ReduceDriver<LongWritable, LogTuple, LongWritable, LongWritable> reduceDriver;
	private MapReduceDriver<LongWritable, Text, LongWritable, LogTuple, LongWritable, LongWritable> mapReduceDriver;

	@Before
	public void setUp() {
		mapDriver = new MapDriver<>(new TomcatLogCount.LogCountMapper());
		reduceDriver = new ReduceDriver<>(new TomcatLogCount.LogCountReducer());
		
		mapReduceDriver = new MapReduceDriver<>();
		mapReduceDriver.setMapper(new TomcatLogCount.LogCountMapper());
		mapReduceDriver.setReducer(new TomcatLogCount.LogCountReducer());
	}

	@Test
	public void testMapper() throws IOException {
		String str = "127.0.0.1 - - [20/Aug/2014:10:26:05 +0900] \"GET / HTTP/1.1\" 404 951";

		mapDriver.withInput(new LongWritable(1), new Text(str));
		mapDriver.withOutput(new LongWritable(1), new LogTuple());
//		mapDriver.runTest();
	}

	@Test
	public void testReducer() throws IOException {
		List<LogTuple> values = new ArrayList<>();
		LogTuple tuple = new LogTuple();
		tuple.setLocalhost("localhost1");
		values.add(tuple);
		tuple.setLocalhost("localhost2");
		values.add(tuple);
		reduceDriver.withInput(new LongWritable(1), values);
		reduceDriver.withOutput(new LongWritable(12), new LongWritable(1));
		// reduceDriver.runTest();
	}

	@Test
	public void testMapReducer() throws IOException {
		String str = "127.0.0.1 - - [20/Aug/2014:10:26:05 +0900] \"GET / HTTP/1.1\" 404 951";

		mapReduceDriver.withInput(new LongWritable(1), new Text(str));
		mapReduceDriver.runTest();
		
	}
}
