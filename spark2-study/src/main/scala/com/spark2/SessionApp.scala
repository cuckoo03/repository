package com.spark2

import org.apache.spark.sql.SparkSession

object SessionApp {
  def main(args: Array[String]) {
    val spark = SparkSession.builder()
      .appName("SessionApp")
      .master("local[*]")
      .config("spark.sql.warehouse.dir", "file:///c:/spark/spark-warehouse")
      .getOrCreate()
    val sc = spark.sparkContext

    println(System.getenv)
    def jsonFile = "C:/Users/cucko/git/repository/spark2-study/2015-03-01-0.json"
    
    def ghLog = spark.read.json(jsonFile)
    println("ghlog:" + ghLog.count)

    val pushes = ghLog.filter("type ='PushEvent'")
    pushes.printSchema()
    println("pushes:" + pushes.count)
    pushes.show(5)
    
    val grouped = pushes.groupBy("actor.login").count
    grouped.show(5)
    
    val logData = sc.textFile("README.md").filter(line => line.contains("a"))
  }
}