package com.spark.groovy.learningspark.ch03

import org.apache.spark.SparkConf
import org.apache.spark.api.java.JavaSparkContext

import groovy.transform.TypeChecked

@TypeChecked
class LoadData {
	static void main(args) {
		def conf = new SparkConf().setAppName("appname").setMaster("local");
		def sc = new JavaSparkContext(conf);
		def data = Arrays.asList(1, 2);
		def distData = sc.parallelize(data);
		
		loadCSV(sc)
	}
	def static void loadCSV(JavaSparkContext sc) {
		def input = sc.textFile("csvfile.csv")
		println input
		println input.count()
	}
}
