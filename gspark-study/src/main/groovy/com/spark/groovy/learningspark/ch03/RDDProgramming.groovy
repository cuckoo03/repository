package com.spark.groovy.learningspark.ch03

import org.apache.spark.SparkConf
import org.apache.spark.api.java.JavaRDD
import org.apache.spark.api.java.JavaSparkContext

import groovy.transform.TypeChecked

@TypeChecked
class RDDProgramming {
	static void main(args) {
		def conf = new SparkConf().setAppName("appname").setMaster("local");
		def sc = new JavaSparkContext(conf);
		def data = Arrays.asList(1, 2);
		def distData = sc.parallelize(data);

		def logFile = "README.md";
		def lines = sc.textFile(logFile);
		def lineLength = lines.map({s -> s.length()});
		def totalLength = lineLength.reduce({ a, b -> a + b });
		println "totalLength:" + totalLength
	}
}
