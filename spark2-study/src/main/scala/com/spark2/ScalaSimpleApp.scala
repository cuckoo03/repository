package com.spark2

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession

object ScalaSimpleApp {
  val logFile = "README.md"
  val conf = new SparkConf().setAppName("Scala Simple application").setMaster("local")
  val sc = new SparkContext(conf)
  val logData = sc.textFile(logFile, 2).cache()
  val numAs = logData.filter(line => line.contains("a")).count()
  val numBs = logData.filter(line => line.contains("b")).count()

  println("a:" + numAs + "," + numBs)
}