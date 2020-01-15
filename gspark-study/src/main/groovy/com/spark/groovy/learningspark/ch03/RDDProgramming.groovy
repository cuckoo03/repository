package com.spark.groovy.learningspark.ch03

import org.apache.spark.SparkConf
import org.apache.spark.api.java.JavaRDD
import org.apache.spark.api.java.JavaSparkContext
import org.apache.spark.api.java.function.DoubleFlatMapFunction
import org.apache.spark.api.java.function.DoubleFunction
import org.apache.spark.api.java.function.FlatMapFunction
import org.apache.spark.api.java.function.PairFlatMapFunction

import groovy.transform.TypeChecked

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
	}
	def static void mapreduce(JavaSparkContext sc) {
		def logFile = "README.md";
		def lines = sc.textFile(logFile);
		def lineLength = lines.map({s -> s.length()});
		def totalLength = lineLength.reduce({ a, b -> a + b });
		println "totalLength:" + totalLength
	}
	def static void flatMap(JavaSparkContext sc) {
		def lines = sc.parallelize(Arrays.asList("1", "2"))
		def func = { String line -> line.split(" ") } as FlatMapFunction
		// 오류 발생
//		def words = lines.flatMap(func)
		def words = lines.flatMap(new FlatMapFunction<String, String>() {
			Iterable<String> call(String line) {
				return Arrays.asList(line.split(" "));
			}
		})
		words.first()
	}
	def static void distinct(JavaSparkContext sc) {
		def lines = sc.parallelize(Arrays.asList("1", "2"))
		println lines.distinct().collect().join(",")
	}
	def static void union(JavaSparkContext sc) {
		def lines = sc.parallelize(Arrays.asList("1"))
		def lines2 = sc.parallelize(Arrays.asList("2", "2"))
		println lines.union(lines2).collect().join(",")
	}
	def static void aggregate(JavaSparkContext sc) {
		
	}
	def static void reduceByKey(JavaSparkContext sc) {
		def list1 = sc.parallelize(["1", "2"])
		def pairs = list1.map({ x -> [x.split(" ")[0], 10] })
		println pairs.collect().join(",")
	}
}
