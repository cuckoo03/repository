package com.spark.groovy_spark

import org.apache.spark.SparkConf
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext

class GroovySimpleApp {
	static void main(args) {
		String logFile = "/usr/local/spark/README.md";
		SparkConf conf = new SparkConf().setAppName("Simple application");
		JavaSparkContext sc = new JavaSparkContext(conf);
		JavaRDD<String> logData = sc.textFile(logFile).filter{ String s ->
			s.contains("a")
		};
		def numAs = logData.count();

		def numBs = sc.textFile(logFile).filter {  String s ->
			s.contains("b")
		}.count();

		System.out.println("Lines with a:" + numAs + ", " + numBs);
	}
}
