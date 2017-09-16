package com;

import java.util.Arrays;
import java.util.List;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;

public class RDDProgramming {

	public static void main(String[] args) {
		SparkConf conf = new SparkConf().setAppName("appname").setMaster(
				"local");
		JavaSparkContext sc = new JavaSparkContext(conf);
		List<Integer> data = Arrays.asList(1, 2);
		JavaRDD<Integer> distData = sc.parallelize(data);

		String logFile = "/usr/local/spark/README.md";
		JavaRDD<String> lines = sc.textFile(logFile);
		JavaRDD<Integer> lineLength = lines.map(s -> s.length());
		int totalLength = lineLength.reduce((a, b) -> a + b);
		System.out.println("totalLength:" + totalLength);

		JavaRDD<Integer> lineLength2 = lines.map(new GetLength());
		int totalLength2 = lineLength2
				.reduce(new Function2<Integer, Integer, Integer>() {
					private static final long serialVersionUID = 1L;

					public Integer call(Integer a, Integer b) {
						return a + b;
					}
				});

		System.out.println("totalLength2:" + totalLength2);
	}
}

class GetLength implements Function<String, Integer> {
	private static final long serialVersionUID = 1L;

	@Override
	public Integer call(String s) throws Exception {
		return s.length();
	}
}