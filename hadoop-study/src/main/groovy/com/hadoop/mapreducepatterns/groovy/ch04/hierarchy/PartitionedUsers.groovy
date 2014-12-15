package com.hadoop.mapreducepatterns.groovy.ch04.hierarchy

import java.text.SimpleDateFormat

import org.apache.hadoop.conf.Configurable
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.IntWritable
import org.apache.hadoop.io.LongWritable
import org.apache.hadoop.io.NullWritable
import org.apache.hadoop.io.Text
import org.apache.hadoop.mapreduce.Job
import org.apache.hadoop.mapreduce.Mapper
import org.apache.hadoop.mapreduce.Partitioner
import org.apache.hadoop.mapreduce.Reducer
import org.apache.hadoop.mapreduce.Mapper.Context
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser

import com.hadoop.mapreducepatterns.MRDPUtils

/**
 * 사용자 정보 셋에 대해 최종 접속 날짜의 연도를 기준으로 년당 하나의 파티션으로 레코드를 분할한다.
 * @author cuckoo03
 *
 */
class PartitionedUsers {
	public static class LastAccessDataMapper extends Mapper<LongWritable, Text,
	IntWritable, Text> {
		private static final SimpleDateFormat sdf = new SimpleDateFormat(
		"yyyy-MM-dd'T'HH:mm:ss.SSS")
		private IntWritable outkey = new IntWritable()
		@Override
		public void map(LongWritable key, Text value, Context context) {
			Map<String, String> parsed = MRDPUtils.transformXmlToMap(
					value.toString())
			String strDate = parsed.get("LastAccessDate")
			if (strDate == null) {
				return
			}

			Calendar cal = Calendar.getInstance()
			cal.setTime(sdf.parse(strDate))
			outkey.set(cal.get(Calendar.YEAR))

			// 년도를 입력 값과 함께 출력
			context.write(outkey, value)
		}
	}
	public static class LastAccessDatePartitioner extends Partitioner<IntWritable,
	Text> implements Configurable {
		private static final String MIN_LAST_ACCESS_DATE_YEAR =
		"min.last.access.date.year"
		private Configuration conf = null
		private int minLastAccessDateYear = 0
		@Override
		public int getPartition(IntWritable key, Text value, int numPartitions) {
			return key.get() - minLastAccessDateYear
		}

		@Override
		public Configuration getConf() {
			return conf
		}

		/**
		 * 이 메서드는 파티셔너를 설정하는 태스크 생성자에서 호출된다
		 */
		@Override
		public void setConf(Configuration conf) {
			this.conf = conf
			minLastAccessDateYear = conf.getInt(MIN_LAST_ACCESS_DATE_YEAR, 0)
		}

		/**
		 * 레코드가 가야할 파티션을 결정하기 위해 각 키(최종 접속 날짜)에서 뺄샘을 하는 데 사용
		 * 파티션 번호는 0부터 시작하도록 값을 설정해야함
		 * @param job
		 * @param minLastAccessDateYear
		 */
		public static void setMinLastAccessDate(Job job,
				int minLastAccessDateYear) {
			job.getConfiguration().setInt(MIN_LAST_ACCESS_DATE_YEAR,
					minLastAccessDateYear)
		}
	}
	public static class ValueReducer extends Reducer<IntWritable, Text, Text,
	NullWritable> {
		@Override
		public void reduce(IntWritable key, Iterable<Text> values,
				org.apache.hadoop.mapreduce.Reducer.Context context) {
			values.each { t ->
				context.write(t, NullWritable.get())
			}
		}
	}
	static main(args) {
		Configuration conf = new Configuration()
		String[] otherArgs = new GenericOptionsParser(conf, args)
				.getRemainingArgs()
		if (otherArgs.length != 2) {
			println "Usage: PartitionedUsers <users> <outdir>"
			System.exit(2)
		}

		Job job = new Job(conf, "PartitionedUsers")
		job.setJarByClass(PartitionedUsers.class)
		job.setMapperClass(LastAccessDataMapper.class)
		// mapper와 reducer의 출력 타입이 다를 경우 output key, value를 설정해 줘야 한다
		job.setMapOutputKeyClass(IntWritable.class)
		job.setMapOutputValueClass(Text.class)
		job.setPartitionerClass(LastAccessDatePartitioner.class)
		LastAccessDatePartitioner.setMinLastAccessDate(job, 2008)
		FileInputFormat.addInputPath(job, new Path(otherArgs[0]))

		job.setReducerClass(ValueReducer.class)
		job.setOutputKeyClass(Text.class)
		job.setOutputValueClass(NullWritable.class)
		job.setOutputFormatClass(TextOutputFormat.class)
		job.setNumReduceTasks(4)

		Path outputDir = new Path(otherArgs[1])
		FileOutputFormat.setOutputPath(job, outputDir)
		org.apache.hadoop.fs.FileSystem.get(conf).delete(outputDir, true)
		
		job.getConfiguration().set("mapred.textoutputformat.separator", "")

		System.exit(job.waitForCompletion(true) ? 0 : 1)
	}
}