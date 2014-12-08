package com.hadoop.doithadoop.ch06;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.junit.Before;
import org.junit.Test;

import com.hadoop.doithadoop.ch06.join.JoinIDTitle;

public class JoinIDTitleTest {
	private MapDriver<Text, Text, Text, Text> mapDriver1;
	private MapDriver<Text, Text, Text, Text> mapDriver2;
	private ReduceDriver<Text, Text, Text, Text> reduceDriver;
	private MapReduceDriver<Text, Text, Text, Text, Text, Text> mapReduceDriver;

	@Before
	public void setUp() {
		JoinIDTitle.MyMapper1 m1 = new JoinIDTitle.MyMapper1();
		JoinIDTitle.MyMapper2 m2 = new JoinIDTitle.MyMapper2();
		JoinIDTitle.MyReducer r = new JoinIDTitle.MyReducer();
		
		mapDriver1 = new MapDriver<>(m1);
		mapDriver2 = new MapDriver<>(m2);
		reduceDriver = new ReduceDriver<>(r);
		mapReduceDriver = new MapReduceDriver<>(m1, r);
	}

	@Test
	public void testMappper1() throws IOException {
		mapDriver1.withInput(new Text("title"), new Text("id"));
		mapDriver1.withOutput(new Text("id"), new Text("title\t1"));
		mapDriver1.runTest();
	}

	@Test
	public void testMapper2() throws IOException {
		mapDriver2.withInput(new Text("id"), new Text("count"));
		mapDriver2.withOutput(new Text("id"), new Text("count\t2"));
		mapDriver2.runTest();
	}
	@Test
	public void testReducer() {
	}

	@Test
	public void testMapReduce() {

	}

}
