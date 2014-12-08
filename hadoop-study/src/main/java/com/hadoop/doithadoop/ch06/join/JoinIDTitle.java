package com.hadoop.doithadoop.ch06.join;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class JoinIDTitle {
	public static class MyMapper1 extends Mapper<Text, Text, Text, Text> {
		/**
		 * input:key 문서 타이틀, value 문서 ID 
		 * output:key 문서 ID, value 문서 타이틀\t+1
		 */
		@Override
		public void map(Text key, Text value, Context context)
				throws IOException, InterruptedException {
			context.write(value, new Text(key + "\t" + 1));
			context.getCounter("Stats", "Number of title+docId").increment(1);
		}
	}

	public static class MyMapper2 extends Mapper<Text, Text, Text, Text> {
		/**
		 * input:key 문서 ID, value 인용 카운트수
		 * output:key 문서ID, value 인용카운트수\t+2
		 */
		@Override
		public void map(Text key, Text value, final Context context)
				throws IOException, InterruptedException {
			context.write(key, new Text(value + "\t" + 2));
			context.getCounter("Stats", "DocId+citation").increment(1);
		}
	}

	public static class MyReducer extends Reducer<Text, Text, Text, Text> {
		/***
		 * input:key 문서ID, value 문서 타이틀\t+1 or 인용카운트수\t+2
		 */
		@Override
		public void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
			String title = null;
			String frequency = null;
			int count = 0;

			for (Text value : values) {
				String str = value.toString();
				String[] tokens = str.split("\\t");
				if (tokens[1].equals("1")) {
					title = tokens[0];
				} else {
					frequency = tokens[0];
				}
				count++;
			}

			if (count == 2 && title != null && frequency != null) {
				context.write(key, new Text(title + "\t" + frequency));
				context.getCounter("Stats", "the number of pairs of matches")
						.increment(1);
			}
		}
	}

	public static void main(String[] args) throws IOException,
			ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();
		Job job = new Job(conf, "JoingIDTitle");

		String docId = args[0]; // 2M.TITLE.ID 파일
		String idFreq = args[1]; // CountCitation/TopN 처리결과
		String outputDir = args[2];

		if (outputDir == null || docId == null || idFreq == null) {
			throw new IllegalArgumentException("Missing parameger");
		}
		job.setJobName("Join ID and Title");
		job.setJarByClass(JoinIDTitle.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		job.setReducerClass(MyReducer.class);

		MultipleInputs.addInputPath(job, new Path(docId),
				KeyValueTextInputFormat.class, MyMapper1.class);
		MultipleInputs.addInputPath(job, new Path(idFreq),
				KeyValueTextInputFormat.class, MyMapper2.class);

		job.setOutputFormatClass(TextOutputFormat.class);

		FileOutputFormat.setOutputPath(job, new Path(outputDir));

		if (!job.waitForCompletion(true)) {
			System.exit(1);
		}
	}
}