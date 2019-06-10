package com

import org.apache.spark.streaming.StreamingContext
import org.apache.spark.SparkContext
import org.apache.spark.streaming.Seconds
import org.apache.spark.SparkConf


object SparkStreamingExam extends App {
  val conf = new SparkConf().setAppName("Scala Simple application")
	val sc = new SparkContext(conf)
  val ssc = new StreamingContext(conf, Seconds(1))
  val lines = ssc.socketTextStream("localhost", 7777)
  var errorLines = lines.filter(_.contains("error"))
  errorLines.print()
  
  ssc.start()
  ssc.awaitTermination()
}