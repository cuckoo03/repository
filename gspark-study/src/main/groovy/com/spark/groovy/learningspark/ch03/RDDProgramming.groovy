package com.spark.groovy.learningspark.ch03

import org.apache.spark.SparkConf
import org.apache.spark.api.java.JavaSparkContext
import org.apache.spark.api.java.function.FlatMapFunction
import org.apache.spark.api.java.function.Function
import org.apache.spark.api.java.function.Function2
import org.apache.spark.api.java.function.PairFunction

import groovy.transform.TypeChecked
import scala.Tuple2

@TypeChecked
class RDDProgramming {
	static void main(args) {
		def conf = new SparkConf().setAppName("appname").setMaster("local");
		def sc = new JavaSparkContext(conf);
		def data = Arrays.asList(1, 2);
		def distData = sc.parallelize(data);

		mapreduce(sc)
		flatMap(sc)
		distinct(sc)
		union(sc)
		aggregate(sc)
		reduceByKey(sc)
		groupByKey(sc)
		mapValues(sc)
		filterPair(sc)
		join(sc)
	}
	def static void mapreduce(JavaSparkContext sc) {
		println "mapreduce"
		def logFile = "README.md";
		def lines = sc.textFile(logFile);
		def lineLength = lines.map({s -> s.length()});
		def totalLength = lineLength.reduce({ a, b -> a + b });
		println "totalLength:" + totalLength
	}
	def static void flatMap(JavaSparkContext sc) {
		println "flatmap"
		def lines = sc.parallelize(Arrays.asList("1", "2"))
		def words = lines.flatMap({ String s -> Arrays.asList(s.split(" ")) })
		words.first()
	}
	def static void distinct(JavaSparkContext sc) {
		println "distinct"
		def lines = sc.parallelize(Arrays.asList("1", "2"))
		println lines.distinct().collect().join(",")
	}
	def static void union(JavaSparkContext sc) {
		println "union"
		def lines = sc.parallelize(Arrays.asList("1"))
		def lines2 = sc.parallelize(Arrays.asList("2", "2"))
		println lines.union(lines2).collect().join(",")
	}
	def static void aggregate(JavaSparkContext sc) {
		
	}
	def static void reduceByKey(JavaSparkContext sc) {
		println "reduceByKey"
		def list1 = sc.parallelize(["1", "2"])
		def pairs = list1.map({ x -> [x.split(" ")[0], 10] })
		def mapToPairs = list1.mapToPair({new Tuple2(it as String, 1)})
		def count = mapToPairs.reduceByKey({Integer i1, Integer i2 -> i1 + i2})
		count.collect().forEach(System.&println)
	}
	def static void groupByKey(JavaSparkContext sc) {
		println "groupByKey"
		def list1 = sc.parallelize(["1", "2"])
		def pairs = list1.mapToPair({new Tuple2(it as String, 1)})
		pairs.groupByKey().collect().forEach(System.&println)
	}
	def static void mapValues(JavaSparkContext sc) {
		println "mapValues"
		def list1 = sc.parallelize(["1", "2"])
		def pairs = list1.mapToPair({new Tuple2(it as String, 1)})
		def values = pairs.mapValues({ Integer x -> x + 10 })
		values.collect().forEach(System.&println)
	}
	def static void filterPair(JavaSparkContext sc) {
		println "filterPair"
		def list1 = sc.parallelize(["1", "2"])
		def pairs = list1.mapToPair({new Tuple2(it as String, 1)})
		def values = pairs.filter({Tuple2<String, Integer> t -> t._1 == "2"})
		values.collect().forEach(System.&println)
	}
	def static void join(JavaSparkContext sc) {
		println "join"
		def list1 = sc.parallelize(["1", "2"])
		def list2 = sc.parallelize(["2", "3"])
		def pairs = list1.mapToPair({new Tuple2(it as String, 11)})
		def pairs2 = list2.mapToPair({new Tuple2(it as String, 111)})
		
		println "join countByKey"
		pairs.join(pairs2).countByKey().forEach(System.&println)
		
		println "join collectAsMap"
		pairs.join(pairs2).collectAsMap().forEach(System.&println)
	}
}