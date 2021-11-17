package com.spark

import org.apache.spark.sql.SparkSession

import groovy.transform.TypeChecked

@TypeChecked
class SimpleApp {
	static main(args) {
		def spark = SparkSession.builder()
				.appName("SessionApp")
				.master("local[*]")
				.config("spark.sql.warehouse.dir", "file:///c:/spark/spark-warehouse")
				.getOrCreate()
		def sc = spark.sparkContext
		
		println System.getenv()
		def jsonFile = "2015-03-01-0.json"
		def ghLog = spark.read().json(jsonFile)
		println("ghlog:" + ghLog.count())
	}
}
