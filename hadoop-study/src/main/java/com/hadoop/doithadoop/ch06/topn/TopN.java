package com.hadoop.doithadoop.ch06.topn;

import java.io.IOException;
import java.util.Comparator;
import java.util.PriorityQueue;

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
import org.apache.hadoop.util.GenericOptionsParser;

public class TopN {
	public static void insert(PriorityQueue<ItemFreq> queue, String item,
			long lValue, int topN) {
		ItemFreq head = queue.peek();

		if (queue.size() < topN || head.getFreq() < lValue) {
			ItemFreq itemfreq = new ItemFreq(item, lValue);
			queue.add(itemfreq);
		}
		if (queue.size() > topN) {
			queue.remove();
		}
	}

	public static class ItemFreqComparator implements Comparator<ItemFreq> {
		@Override
		public int compare(ItemFreq x, ItemFreq y) {
			if (x.getFreq() < y.getFreq()) {
				return -1;
			}
			if (x.getFreq() > y.getFreq()) {
				return 1;
			}
			return 0;
		}

	}

	public static class Map extends Mapper<Text, Text, Text, LongWritable> {
		private final static LongWritable one = new LongWritable(1);
		Comparator<ItemFreq> comparator = new ItemFreqComparator();
		PriorityQueue<ItemFreq> queue = new PriorityQueue<ItemFreq>(10,
				comparator);
		int topN = 10;

		@Override
		public void map(Text key, Text value, Context context) {
			Long lValue = (long) Integer.parseInt(value.toString());
			insert(queue, key.toString(), lValue, topN);
		}

		@Override
		protected void setup(Context context) {
			topN = context.getConfiguration().getInt("topN", 10);
		}

		@Override
		protected void cleanup(Context context) throws IOException,
				InterruptedException {
			while (queue.size() != 0) {
				ItemFreq item = queue.remove();
				context.write(new Text(item.getItem()),
						new LongWritable(item.getFreq()));
			}
		}
	}

	public static class Reduce extends
			Reducer<Text, LongWritable, Text, LongWritable> {
		Comparator<ItemFreq> comparator = new ItemFreqComparator();
		PriorityQueue<ItemFreq> queue = new PriorityQueue<ItemFreq>(10,
				comparator);
		int topN = 10;

		@Override
		protected void setup(Context context) {
			topN = context.getConfiguration().getInt("topN", 10);
		}

		@Override
		protected void cleanup(Context context) throws IOException,
				InterruptedException {
			while (queue.size() != 0) {
				ItemFreq item = queue.remove();
				context.write(new Text(item.getItem()),
						new LongWritable(item.getFreq()));
			}
		}

		@Override
		public void reduce(Text key, Iterable<LongWritable> values,
				Context context) {
			long sum = 0;
			for (LongWritable val : values) {
				sum += val.get();
			}
			insert(queue, key.toString(), sum, topN);
		}
	}

	public static void main(String[] args) throws IOException,
			ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();
//		String[] otherArgs = new GenericOptionsParser(conf, args)
//				.getRemainingArgs();
		Job job = new Job(conf, "TopN");

		job.setJarByClass(TopN.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(LongWritable.class);

		job.setMapperClass(Map.class);
		job.setReducerClass(Reduce.class);
		job.setNumReduceTasks(1);

		job.setInputFormatClass(KeyValueTextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);

		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		job.getConfiguration().setInt("topN", Integer.parseInt(args[2]));

		job.waitForCompletion(true);
	}
}