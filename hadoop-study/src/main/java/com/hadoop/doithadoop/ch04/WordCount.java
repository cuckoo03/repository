package com.hadoop.doithadoop.ch04;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.mapreduce.lib.reduce.LongSumReducer;
import org.apache.log4j.Logger;

public class WordCount {
	public static class MyMapper extends
			Mapper<LongWritable, Text, Text, LongWritable> {
		private static final Logger log = org.apache.log4j.Logger
				.getLogger(WordCount.class.getName());
		private final static LongWritable one = new LongWritable(1);
		private Text word = new Text();

		enum RecordType {
			COUNT
		}

		@Override
		public void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			log.info("*******start");
			log.warn("***********");
			log.fatal("*******end*");
			String line = value.toString();
			if (line != null) {
				System.err.println("Record input:" + value);
				context.setStatus("record input count");
				context.getCounter(RecordType.COUNT).increment(1);
			}
			StringTokenizer tokenizer = new StringTokenizer(line,
					"\t\r\n\f|,.()<> ");
			while (tokenizer.hasMoreElements()) {
				word.set(tokenizer.nextToken().toLowerCase());
				context.write(word, one);
			}
		}
	}

	public static class MyReducer extends
			Reducer<Text, LongWritable, Text, LongWritable> {
		private LongWritable sumWritable = new LongWritable();

		@Override
		public void reduce(Text key, Iterable<LongWritable> values,
				Context context) throws IOException, InterruptedException {
			long sum = 0;
			for (LongWritable val : values) {
				sum += val.get();
			}
			sumWritable.set(sum);
			context.write(key, sumWritable);
		}
	}

	public static void main(String[] args) throws IOException,
			ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();
		Job job = new Job(conf, "WordCount");

		job.setJarByClass(WordCount.class);
		job.setMapperClass(MyMapper.class);
		job.setInputFormatClass(TextInputFormat.class);
		FileInputFormat.addInputPath(job, new Path(args[0]));

		// job.setReducerClass(MyReducer.class);
		job.setReducerClass(LongSumReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(LongWritable.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		FileSystem.get(conf).delete(new Path(args[1]), true);
		System.out.println(args[1] + " directory deleted.");

		job.waitForCompletion(true);
	}
}