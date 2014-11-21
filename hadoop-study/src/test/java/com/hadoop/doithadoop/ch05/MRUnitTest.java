package com.hadoop.doithadoop.ch05;

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

import com.hadoop.doithadoop.ch04.WordCount;

public class MRUnitTest {
	private MapDriver<LongWritable, Text, Text, LongWritable> mapDriver;
	private ReduceDriver<Text, LongWritable, Text, LongWritable> reduceDriver;
	private MapReduceDriver<LongWritable, Text, Text, LongWritable, Text, LongWritable> mapReduceDriver;

	@Before
	public void setUp() {
		WordCount.MyMapper m = new WordCount.MyMapper();
		WordCount.MyReducer r = new WordCount.MyReducer();

		mapDriver = new MapDriver<>();
		mapDriver.setMapper(m);
		reduceDriver = new ReduceDriver<>();
		reduceDriver.setReducer(r);

		mapReduceDriver = new MapReduceDriver<>();
		mapReduceDriver.setMapper(m);
		mapReduceDriver.setReducer(r);
	}

	@Test
	public void testMapper() throws IOException {
//		mapDriver.withInput(new LongWritable(), new Text("cat cat dog"));
//		mapDriver.withOutput(new Text("cat"), new LongWritable(1));
		// mapDriver.withOutput(new Text("cat"), new LongWritable(1));
		// mapDriver.withOutput(new Text("dog"), new LongWritable(1));

//		mapDriver.runTest();
	}

	@Test
	public void testReducer() throws IOException {
//		List<LongWritable> values = new ArrayList<LongWritable>();
//		values.add(new LongWritable(1));
//		values.add(new LongWritable(1));
//		reduceDriver.withInput(new Text("cat"), values);
//		reduceDriver.withOutput(new Text("cat"), new LongWritable(2));

//		reduceDriver.runTest();
	}

	@Test
	public void testMapReduce() throws IOException {
//		mapReduceDriver.withInput(new LongWritable(1), new Text("cat cat dog"));
//		mapReduceDriver.addOutput(new Text("cat"), new LongWritable(2));
//		mapReduceDriver.addOutput(new Text("dog"), new LongWritable(1));

//		mapReduceDriver.runTest();
	}

}