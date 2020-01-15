package com

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object ScalaRDDProgramming extends App {
  val conf = new SparkConf().setAppName("appName").setMaster("local")
  val sc = new SparkContext(conf)

  val data = Array(1, 2)
  val distData = sc.parallelize(data)

  val logFile = "README.md"
  val lines = sc.textFile(logFile)
  val lineLength = lines.map(s => s.length())
  val totalLength = lineLength.reduce((a, b) => a + b)
  println("totalLength:" + totalLength)

  val input = sc.parallelize(List(1, 2, 3))
  val mapResult = input.map(x => x * 2)
  println(mapResult.collect().mkString(","))

  flatMap(sc)
  distinct(sc)
  union(sc)
  countByValue(sc)
  pair(sc)

  def flatMap(sc: SparkContext): Unit = {
    val lines = sc.parallelize(List("1", "2"))
    var words = lines.flatMap(line => line.split(" "))
    words.first()
  }
  def distinct(sc: SparkContext): Unit = {
    val lines = sc.parallelize(List("1", "2", "2"))
    def dis = lines.distinct()
    dis.collect().mkString(",").foreach(println(_))
  }
  def union(sc: SparkContext): Unit = {
    val lines = sc.parallelize(List("1"))
    val lines2 = sc.parallelize(List("2", "2"))
    println(lines.union(lines2).collect().mkString(","))
  }
  def aggregate(sc: SparkContext): Unit = {
    val lines = sc.parallelize(List("1", "2"))
    var r = lines.aggregate((0, 0))(
      (x, y) => (x._1 + 1, x._2 + 1),
      (x, y) => (x._1 + y._1, x._2 + y._2))
    println(r)
  }
  def countByValue(sc: SparkContext): Unit = {
    val lines = sc.parallelize(List("1", "2", "2"))
    println(lines.countByValue())
  }
  def pair(sc: SparkContext): Unit = {
    val lines = sc.parallelize(List("1", "2"))
    val pairs = lines.map(x => (x.split(" ")(0), 10))
    println(pairs.collect().mkString(","))
  }
}