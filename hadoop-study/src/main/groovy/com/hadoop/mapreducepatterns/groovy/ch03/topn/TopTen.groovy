package com.hadoop.mapreducepatterns.groovy.ch03.topn

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.LongWritable
import org.apache.hadoop.io.NullWritable
import org.apache.hadoop.io.Text
import org.apache.hadoop.mapreduce.Job
import org.apache.hadoop.mapreduce.Mapper
import org.apache.hadoop.mapreduce.Reducer
import org.apache.hadoop.mapreduce.Mapper.Context
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat
import org.apache.hadoop.util.GenericOptionsParser

import com.hadoop.mapreducepatterns.MRDPUtils

/**
 * 주어진 사용자 정보 목록에서 평판을 기준으로 톱텐 사용자 정보를 출력한다.
 * input users.xml
 * @author cuckoo03
 *
 */
class TopTen{
	public static class TopTenMapper extends Mapper<LongWritable, Text, NullWritable,
	Text> {
		private TreeMap<Integer, Text> repToRecordMap = new TreeMap<Integer, Text>()

		@Override
		public void map(LongWritable key, Text value, Context context) {
			Map<String, String> parsed = MRDPUtils.transformXmlToMap(
					value.toString())

			if (parsed == null) {
				return
			}
			String userId = parsed.get("Id")
			String reputation = parsed.get("Reputation")
			println "$userId , $reputation"

			if (userId == null || reputation == null) {
				return
			}

			// 평판을 키로 하여 레코드를 맵에 추가
			repToRecordMap.put(Integer.parseInt(reputation), new Text(value))

			// 열 개 이상의 레코드가 있으면 가장 낮은 평판을 가진 레코드를 제거
			// 트리맵은 내림차순으로 정렬되어 잇기 때문에 가장 낮은 평판을 가진 사용자는 첫 번째 키다
			if (repToRecordMap.size() > 10) {
				repToRecordMap.remove(repToRecordMap.firstKey())
			}
		}
		@Override
		protected void cleanup(Context context) {
			repToRecordMap.values().each { t ->
				println t.toString()
				context.write(NullWritable.get(), t)
			}
		}
	}
	public static class TopTenReducer extends Reducer<NullWritable, Text,
	NullWritable, Text>{
		private TreeMap<Integer, Text> repToRecordMap = new TreeMap<>()

		@Override
		public void reduce(NullWritable key, Iterable<Text> values,
				org.apache.hadoop.mapreduce.Reducer.Context context) {
			values.each { value ->
				Map<String, String> parsed = MRDPUtils.transformXmlToMap(
						value.toString())
				repToRecordMap.put(Integer.parseInt(parsed.get("Reputation")),
						new Text(parsed.get("Id")))

				// 열 개 이상의 레코드가 있으면 가장 낮은 평판을 가진 레코드를 제거
				// 트리맵은 내림차순으로 정렬되어 잇기 때문에 가장 낮은 평판을 가진 사용자는 첫 번째 키다
				if (repToRecordMap.size() > 10) {
					repToRecordMap.remove(repToRecordMap.firstKey())
				}
			}

			String value = null;
			repToRecordMap.descendingMap().keySet().collect().each { it ->
				value = "id:, " + repToRecordMap.descendingMap().getAt(it) + ", reputation:$it"
				// null을 키로하여 톱텐 레코드를 파일 시스템에 출력
				context.write(NullWritable.get(), value)
			}
			println "**********************"
			println "record size:" + repToRecordMap.values().size()
		}
	}
	static main(args) {
		Configuration conf = new Configuration()
		String[] otherArgs = new GenericOptionsParser(conf, args)
				.getRemainingArgs()
		if (otherArgs.length != 2) {
			println "Usage: TopTen <in> <out>"
			System.exit(2)
		}

		Job job = new Job(conf, "TopTen Users by Reputation")
		job.setJarByClass(TopTen.class)
		job.setMapperClass(TopTenMapper.class)
		FileInputFormat.addInputPath(job, new Path(otherArgs[0]))

		job.setOutputKeyClass(NullWritable.class)
		job.setOutputValueClass(Text.class)
		job.setNumReduceTasks(1)
		Path outputDir = new Path(otherArgs[1])
		job.setReducerClass(TopTenReducer.class)
		FileOutputFormat.setOutputPath(job, outputDir)

		FileSystem.get(conf).delete(outputDir, true);

		System.exit(job.waitForCompletion(true) ? 0 : 1)
	}
}