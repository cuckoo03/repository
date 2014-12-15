package com.hadoop.mapreducepatterns.groovy.ch03.distinct

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.LongWritable
import org.apache.hadoop.io.NullWritable
import org.apache.hadoop.io.Text
import org.apache.hadoop.mapreduce.Job
import org.apache.hadoop.mapreduce.Mapper
import org.apache.hadoop.mapreduce.Reducer
import org.apache.hadoop.mapreduce.Mapper.Context
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat
import org.apache.hadoop.util.GenericOptionsParser
import org.apache.hadoop.fs.FileSystem

import com.hadoop.mapreducepatterns.MRDPUtils

/**
 * 사용자 코멘트 목록에서 사용자 아이디를 구별한 집합을 구한다
 * 맵의 출력 키를 Text로 할경우 리듀스에서 정렬이 되지 않는다.
 * @author cuckoo03
 *
 */
class DistinctUser {
	public static class DistinctUserMapper extends Mapper<LongWritable, Text,
	LongWritable, NullWritable> {
		private LongWritable outUserId = new LongWritable()
		@Override
		public void map(LongWritable key, Text value, Context context) {
			Map<String, String> parsed = MRDPUtils.transformXmlToMap(
					value.toString())
			String userId = parsed.get("UserId")
			if (userId == null) {
				return
			}
			outUserId.set(Long.parseLong(userId))

			context.write(outUserId, NullWritable.get())
		}
	}
	public static class DistinctUserReducer extends Reducer<LongWritable, NullWritable,
	LongWritable, NullWritable> {
		@Override
		public void reduce(LongWritable key, Iterable<NullWritable> values,
				org.apache.hadoop.mapreduce.Reducer.Context context) {
			context.write(key, NullWritable.get())
		}
	}
	static main(args) {
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args)
				.getRemainingArgs();
		if (otherArgs.length != 2) {
			System.err.println("Usage: DistinctUser <in> <out>");
			System.exit(2);
		}

		Job job = new Job(conf, "StackOverflow Distinct Users");
		job.setJarByClass(DistinctUser.class);
		job.setMapperClass(DistinctUserMapper.class);
		job.setCombinerClass(DistinctUserReducer.class);
		FileInputFormat.addInputPath(job, new Path(otherArgs[0]));

		job.setOutputKeyClass(LongWritable.class);
		job.setOutputValueClass(NullWritable.class);
		job.setReducerClass(DistinctUserReducer.class);
		Path outputDir = new Path(otherArgs[1])
		FileOutputFormat.setOutputPath(job, outputDir)

		FileSystem.get(conf).delete(outputDir, true)

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}