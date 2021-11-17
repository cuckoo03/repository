package com.spark.groovy.learningspark.ch03

import org.apache.spark.SparkConf
import org.apache.spark.api.java.JavaRDD
import org.apache.spark.api.java.JavaSparkContext

import groovy.transform.TypeChecked

@TypeChecked
class SimpleApp {
	static main(args) {
		def logFile = "README.md";
		def conf = new SparkConf().setAppName("Simple application")
				.setMaster("local");
		def sc = new JavaSparkContext(conf);
		def logData = sc.textFile(logFile).filter({s -> s.contains("a")});
		def numAs = logData.count();

		def numBs = sc.textFile(logFile).filter({s -> s.contains("b")}).count();

		System.out.println("Lines with a:" + numAs + ", b:" + numBs);
	}
}
