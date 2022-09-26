package com.spark.groovy.learningspark.ch03

import org.apache.spark.Accumulator
import org.apache.spark.SparkConf
import org.apache.spark.api.java.JavaSparkContext
import org.apache.spark.api.java.function.FlatMapFunction

import groovy.transform.TypeChecked

@TypeChecked
class AccumulatorMain {
	static void main(args) {
		def conf = new SparkConf().setAppName("appname").setMaster("local");
		def sc = new JavaSparkContext(conf);
		def data = Arrays.asList(1, 2);
		def distData = sc.parallelize(data);
		
		accumulate(sc)
	}
	
	// 파일에서 빈줄 횟수 세기
	def static void accumulate(JavaSparkContext sc) {
		def Accumulator<Integer> acc = sc.accumulator(0)
		def logFile = "README.md";
		def lines = sc.textFile(logFile)
		def numBlankLine = lines.flatMap({String line -> 
			if (line == "")
				acc.add(1)
			Arrays.asList(line.split(" "))
		})
		
		def dir = new File("output.dir")
		if (dir.exists()) {
			println "dir is exist."
			return
		} 
		numBlankLine.saveAsTextFile("output.dir")
		println acc.value()
	}
}