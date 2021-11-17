package com.spark2;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

public class SimpleApp {
	public static void main(String[] args) {
		String logFile = "README.md";
		SparkConf conf = new SparkConf().setAppName("Simple application")
				.setMaster("local");
		JavaSparkContext sc = new JavaSparkContext(conf);
		JavaRDD<String> logData = sc.textFile(logFile).filter(s -> s.contains("a"));
		long numAs = logData.count();

		long numBs = sc.textFile(logFile).filter(s -> s.contains("b")).count();

		System.out.println("Lines with a:" + numAs + ", b:" + numBs);
	}
}