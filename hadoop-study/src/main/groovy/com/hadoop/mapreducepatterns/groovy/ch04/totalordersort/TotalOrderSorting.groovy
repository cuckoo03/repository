package com.hadoop.mapreducepatterns.groovy.ch04.totalordersort

import org.apache.hadoop.conf.Configuration
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
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat
import org.apache.hadoop.mapreduce.lib.partition.InputSampler
import org.apache.hadoop.mapreduce.lib.partition.TotalOrderPartitioner
import org.apache.hadoop.util.GenericOptionsParser

import com.hadoop.mapreducepatterns.MRDPUtils
/**
 * 사용자 셋 데이터를 최근 방문을 기준으로 정렬
 * users.xml row count:5000
 * samplingRate:50
 * @author cuckoo03
 *
 */
class TotalOrderSorting {
	public static class LastAccessDateMapper extends Mapper<LongWritable, Text, 
	Text, Text> {
		private Text outkey = new Text()
		@Override
		public void map(LongWritable key, Text value, Context context) {
			Map<String, String> parsed = MRDPUtils.transformXmlToMap(
					value.toString())
			String date = parsed.get("LastAccessDate")
			if (date == null) {
				return
			}
			outkey.set(date)

			context.write(outkey, value)
		}
	}
	public static class ValueReducer extends Reducer<Text, Text, Text,
	NullWritable> {
		@Override
		public void reduce(Text key, Iterable<Text> values,
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
		if (otherArgs.length != 3) {
			println "Usage:TotalOrderSorting <user data> <out dir> <num of sample>"
			println" TotalOrderSorting users.xml /output 10"
			System.exit(2)
		}
		Path inputPath = new Path(otherArgs[0])
		Path partitionFile = new Path(otherArgs[1] + "_partitions.lst")
		Path outputStage = new Path(otherArgs[1] + "_staging")
		Path outputOrder = new Path(otherArgs[1])
		double sampleRate = Double.parseDouble(otherArgs[2])

		FileSystem.get(conf).delete(outputStage, true)
		FileSystem.get(conf).delete(outputOrder, true)
		FileSystem.get(conf).delete(partitionFile, true)

		Job sampleJob = new Job(conf, "TotalOrderSorting")
		sampleJob.setJarByClass(TotalOrderSorting.class)

		sampleJob.setMapperClass(LastAccessDateMapper.class)
		sampleJob.setNumReduceTasks(0)
		FileInputFormat.addInputPath(sampleJob, inputPath)

		sampleJob.setOutputKeyClass(Text.class)
		sampleJob.setOutputValueClass(Text.class)
		// 출력 포맷으로 sequence 파일 설정
		sampleJob.setOutputFormatClass(SequenceFileOutputFormat.class)
		SequenceFileOutputFormat.setOutputPath(sampleJob, outputStage)

		int code = sampleJob.waitForCompletion(true) ? 0 : 1
		println "************************"
		println "sampleJob end code:$code"
		
		if (code == 0) {
			Job orderJob = new Job(conf, "TotalOrderSortingStage")
			orderJob.setJarByClass(TotalOrderSorting.class)

			// TextInputFormat만 생략 가능 그외 inputformat은 설정해야함
			// 미설정시 
			// java.io.IOException: wrong key class: org.apache.hadoop.io.LongWritable is not class org.apache.hadoop.io.Text
			orderJob.setInputFormatClass(SequenceFileInputFormat.class)
			// SequenceFile에 키/값 쌍으르 출력하기 위해 아이덴티티 매퍼를 사용
			orderJob.setMapperClass(Mapper.class)

			// 하둡의 TotalOrderPartitioner 사용
			orderJob.setPartitionerClass(TotalOrderPartitioner.class)
			// 파티션 파일 설정
			TotalOrderPartitioner.setPartitionFile(orderJob.getConfiguration(),
					partitionFile)
			// 이전 잡의 출력을 입력으로 설정
			SequenceFileInputFormat.setInputPaths(orderJob, outputStage)

			orderJob.setReducerClass(ValueReducer.class)
			// 정렬할 데이터의 양에 적당한 숫자로 리듀스 태스크 개수를 설정
			orderJob.setNumReduceTasks(5)
			orderJob.setOutputKeyClass(Text.class)
			orderJob.setOutputValueClass(Text.class)
			TextOutputFormat.setOutputPath(orderJob, outputOrder)

			// 구분자로 공백 문자 설정
			orderJob.getConfiguration().set("mapred.textoutputformat.separator",
					"")

			// inputSampler를 사용하여 이전 잡의 출력을 통해 표본을 추출하고 파티션 파일 설정
			InputSampler.writePartitionFile(orderJob,
					new InputSampler.RandomSampler(sampleRate, 5000))

			code = orderJob.waitForCompletion(true) ? 0 : 2
		}

		// 파티션 파일과 스테이징 디렉토리 정리
//		FileSystem.get(conf).delete(partitionFile, false)
//		FileSystem.get(conf).delete(outputStage, true)

		System.exit(code)
	}
}