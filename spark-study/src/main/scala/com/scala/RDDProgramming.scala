package com.scala

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object RDDProgramming extends App {
	val conf = new SparkConf().setAppName("appName").setMaster("local")
	val sc = new SparkContext(conf)

	val data = Array(1, 2)
	val distData = sc.parallelize(data)

	val logFile = "/usr/local/spark/README.md"
	val lines = sc.textFile(logFile)
	val lineLength = lines.map(s => s.length())
	val totalLength = lineLength.reduce((a, b) => a + b)
	println("totalLength:" + totalLength)

}