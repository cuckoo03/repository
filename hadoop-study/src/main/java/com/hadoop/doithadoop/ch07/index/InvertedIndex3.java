package com.hadoop.doithadoop.ch07.index;

import java.io.IOException;
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

public class InvertedIndex3 {
	public static class Map extends Mapper<Text, Text, WordID, Text> {
		private WordID wordID = new WordID();

		@Override
		public void map(Text key, Text value, Context context)
				throws IOException, InterruptedException {
			String line = value.toString();
			StringTokenizer tokenizer = new StringTokenizer(line, "\t\r\n");
			while (tokenizer.hasMoreTokens()) {
				wordID.setWord(tokenizer.nextToken().toLowerCase());
				try {
					wordID.setDocID(Long.parseLong(key.toString()));
				} catch (Exception e) {
					context.getCounter("Error", "DocID conversion error")
							.increment(1);
				}
				context.write(wordID, key);
			}
		}
	}

	public static class Reduce extends Reducer<WordID, Text, Text, Text> {
		@Override
		public void reduce(WordID key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
			try {
				String word = key.getWord();
				StringBuilder toReaturn = new StringBuilder();
				boolean first = true;
				String prevDocID = "";

				for (Text val : values) {
					String curDocID = val.toString();
					if (!curDocID.equals(prevDocID)) {
						if (!first) {
							toReaturn.append(",");
						} else {
							first = false;
						}
						toReaturn.append(val.toString());
						prevDocID = curDocID;
					}
				}
				context.write(new Text(word), new Text(toReaturn.toString()));
			} catch (Exception e) {
				context.getCounter("Error",
						"ReducerException:" + key.toString()).increment(1);
			}
		}
	}

	public static void main(String[] args) throws IOException,
			ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();
		Job job = new Job(conf, "Inverted Index3");

		job.setJarByClass(InvertedIndex3.class);
		
		job.setMapOutputKeyClass(WordID.class);
		job.setMapOutputValueClass(Text.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		job.setMapperClass(Map.class);
		job.setReducerClass(Reduce.class);

		job.setPartitionerClass(WordIDPartitioner.class);
		job.setGroupingComparatorClass(WordIDGroupingComparator.class);
		job.setSortComparatorClass(WordIDSortComparator.class);

		job.setInputFormatClass(KeyValueTextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		job.setNumReduceTasks(10);
		
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		
		job.waitForCompletion(true);
	}
}