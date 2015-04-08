package com.java;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;

public class WordCount {
	private static final Pattern SPACE = Pattern.compile(" ");

	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName("wordCount");
		JavaSparkContext sc = new JavaSparkContext(conf);
		String logFile = "/usr/local/spark/README.md";
		JavaRDD<String> lines = sc.textFile(logFile);

		JavaRDD<String> words = lines
				.flatMap(s -> Arrays.asList(SPACE.split(s)));
		JavaPairRDD<String, Integer> ones = words.mapToPair(s -> new Tuple2<>(
				s, 1));
		JavaPairRDD<String, Integer> counts = ones
				.reduceByKey((int1, int2) -> int1 + int2);
		List<Tuple2<String, Integer>> output = counts.collect();

		for (Tuple2<String, Integer> tuple : output) {
			System.out.println(tuple._1() + ":" + tuple._2());
		}

		sc.stop();
	}
}
