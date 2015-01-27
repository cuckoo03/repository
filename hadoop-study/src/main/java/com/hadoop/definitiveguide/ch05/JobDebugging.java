package com.hadoop.definitiveguide.ch05;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.log4j.Logger;

import com.hadoop.doithadoop.ch04.WordCount;

public class JobDebugging {
	public static class MyMapper extends
			Mapper<LongWritable, Text, Text, LongWritable> {
		private static final Logger log = org.apache.log4j.Logger
				.getLogger(WordCount.class.getName());
		private final static LongWritable one = new LongWritable(1);
		private Text word = new Text();

		enum NullType {
			A
		}
		@Override
		public void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			log.info("*******start");
			log.warn("***********");
			log.fatal("*******end*");
			String line = value.toString();
			if (line.equals("A")) {
				context.setStatus("value is A");
				context.getCounter(NullType.A).increment(1);
			}
			StringTokenizer tokenizer = new StringTokenizer(line,
					"\t\r\n\f|,.()<> ");
			while (tokenizer.hasMoreElements()) {
				word.set(tokenizer.nextToken().toLowerCase());
				context.write(word, one);
			}
		}
	}
}
