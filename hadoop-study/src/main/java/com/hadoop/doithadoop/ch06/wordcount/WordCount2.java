package com.hadoop.doithadoop.ch06.wordcount;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class WordCount2 {
	public static class MyMapper extends Mapper<Text, Text, Text, LongWritable> {
		private final static LongWritable one = new LongWritable(1);
		private Text word = new Text();

		@Override
		public void map(Text key, Text value, Context context)
				throws IOException, InterruptedException {
			String line = value.toString();
			StringTokenizer tokenizer = new StringTokenizer(line,
					"\t\r\n\f|,.()<>");
			while (tokenizer.hasMoreTokens()) {
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
			context.getCounter("words stats", "unique words").increment(1);
		}
	}

	public static void main(String[] args) throws IOException,
			ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();
		Job job = new Job(conf, "WordCount2");

		job.setJarByClass(WordCount2.class);
		job.setMapperClass(MyMapper.class);

		job.setCombinerClass(MyReducer.class);
		job.setReducerClass(MyReducer.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(LongWritable.class);

		job.setInputFormatClass(KeyValueTextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		job.waitForCompletion(true);
	}
}
