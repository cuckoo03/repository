package com.hadoop.doithadoop.ch07.index;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class InvertedIndex2 {
	public static class Map extends Mapper<Text, Text, Text, Text> {
		private Set<String> words = new HashSet<>();
		private Text word = new Text();

		@Override
		public void map(Text key, Text value, Context context)
				throws IOException, InterruptedException {
			String line = value.toString();
			StringTokenizer tokenizer = new StringTokenizer(line, "\t\r\n");
			
			words.clear();
			while (tokenizer.hasMoreTokens()) {
				words.add(tokenizer.nextToken().toLowerCase());
			}
			
			Iterator<String> iter = words.iterator();
			while(iter.hasNext()) {
				String v = iter.next();
				word.set(v);
				context.write(word, key);
			}
		}
	}

	public static class Reduce extends Reducer<Text, Text, Text, Text> {
		@Override
		public void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
			try {
				StringBuilder toReturn = new StringBuilder();
				boolean first = true;

				for (Text val : values) {
					if (!first) {
						toReturn.append(",");
					} else {
						first = false;
					}
					toReturn.append(val.toString());
				}
				context.write(key, new Text(toReturn.toString()));
			} catch (Exception e) {
				context.getCounter("Error",
						"Reducer Exception:" + key.toString()).increment(1);
			}
		}
	}

	public static void main(String[] args) throws IOException,
			ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();
		Job job = new Job(conf);

		job.setJarByClass(InvertedIndex2.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		
		job.setInputFormatClass(KeyValueTextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		job.setNumReduceTasks(10);
		
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		job.waitForCompletion(true);
	}
}