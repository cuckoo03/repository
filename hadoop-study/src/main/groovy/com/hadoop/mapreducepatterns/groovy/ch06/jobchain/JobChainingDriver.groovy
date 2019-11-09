package com.hadoop.mapreducepatterns.groovy.ch06.jobchain

import groovy.transform.TypeChecked;

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.filecache.DistributedCache
import org.apache.hadoop.fs.FileStatus
import org.apache.hadoop.fs.Path
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.io.LongWritable
import org.apache.hadoop.io.Text
import org.apache.hadoop.mapreduce.Job
import org.apache.hadoop.mapreduce.Mapper
import org.apache.hadoop.mapreduce.Reducer
import org.apache.hadoop.mapreduce.Mapper.Context
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat
import org.apache.hadoop.mapreduce.lib.reduce.LongSumReducer

import com.hadoop.mapreducepatterns.MRDPUtils

/**
 * 게시글의 데이터 셋에서 사용자마다 평균 게시글 수 이상인지 또는 이하인지를 기준으로 사용자를 빈에 분류한다.
 * 또한 출력을 생성할 때 별개의 데이터 셋에 있는 평판 정보를 사용자 데이터에 추가한다.
 * output
 * above/part-0
 * userid	num post	reputation
 * 
 * below/part-0
 * userid	num post	reputation
 * 
 * Usage:JobChaning posts users outputdir
 * @author cuckoo03
 *
 */
@TypeChecked
class JobChainingDriver {
	public static final String AVERAGE_CALC_GROUP = "AverageCalculation"
	public static final String MULTIPLE_OUTPUTS_ABOVE_NAME = "aboveavg"
	public static final String MULTIPLE_OUTPUTS_BELOW_NAME = "belowavg"

	public static class UserIdCountMapper extends Mapper<LongWritable, Text,
	Text, LongWritable> {
		public static final String RECORDS_COUNTER_NAME = "Records"
		private static final LongWritable ONE = new LongWritable(1)
		private Text outkey = new Text()

		@Override
		public void map(LongWritable key, Text value, Context context) {
			Map<String, String> parsed = MRDPUtils.transformXmlToMap(
					value.toString())
			String userId = parsed.get("OwnerUserId")
			if (userId != null) {
				outkey.set(userId)
				context.write(outkey, ONE)
				// 이 값을 나중에 드라이버에서 사용자당 평균 게시글 수를 계산할 때 사용
				context.getCounter(JobChainingDriver.AVERAGE_CALC_GROUP,
						RECORDS_COUNTER_NAME).increment(1)
			}
		}
	}
	public static class UserIdSumReducer extends Reducer<Text, LongWritable,
	Text, LongWritable> {
		public static final String USERS_COUNTER_NAME = "Users"
		private LongWritable outvalue = new LongWritable()
		@Override
		public void reduce(Text key, Iterable<LongWritable> values,
				org.apache.hadoop.mapreduce.Reducer.Context context) {
			// 각 리듀스 그룹은 한 명의 사용자를 나타내기 때문에 사용자 카운터를 증가
			context.getCounter(JobChainingDriver.AVERAGE_CALC_GROUP,
					USERS_COUNTER_NAME).increment(1)

			int sum = 0
			values.each { value ->
				sum += value.get().toInteger()
			}

			outvalue.set(sum)
			// key:id, value:count
			context.write(key, outvalue)
		}
	}

	public static class UserIdBinningMapper extends Mapper<LongWritable,
	Text, Text, Text> {
		public static final String AVERAGE_POSTS_PER_USER = "avg.posts.per.user"

		public static void setAveragePostsPerUser(Job job, double avg) {
			job.getConfiguration().set(AVERAGE_POSTS_PER_USER,
					Double.toString(avg))
		}

		public static double getAveragePostsPerUser(Configuration conf) {
			return Double.parseDouble(conf.get(AVERAGE_POSTS_PER_USER))
		}

		private double average = 0.0
		private MultipleOutputs<Text, Text> mos = null
		private Text outkey = new Text()
		private Text outvalue = new Text()
		private Map<String, String> userIdToReputation = new HashMap<>()

		@Override
		public void map(LongWritable key, Text value, Context context) {
			String[] tokens = value.toString().split("\t")

			String userId = tokens[0]
			int posts = Integer.parseInt(tokens[1])

			outkey.set(userId)
			outvalue.set(posts + "\t" + userIdToReputation.get(userId))

			// 사용자의 게시글 수가 평균 이상, 이하인지를 기준으로 사용자 정보에 대한 디렉토리에 데이터를 쓴다
			if ((double) posts < average) {
				mos.write(JobChainingDriver.MULTIPLE_OUTPUTS_BELOW_NAME,
						outkey,outvalue,
						JobChainingDriver.MULTIPLE_OUTPUTS_BELOW_NAME + "/part")
			} else {
				mos.write(JobChainingDriver.MULTIPLE_OUTPUTS_ABOVE_NAME,
						outkey, outvalue,
						JobChainingDriver.MULTIPLE_OUTPUTS_ABOVE_NAME + "/part")
			}
		}

		@Override
		public void setup(Context context) {
			average = getAveragePostsPerUser(context.getConfiguration())
			mos = new MultipleOutputs<>(context)

			// load users.xml
			Path[] files = DistributedCache.getLocalCacheFiles(context.getConfiguration())

			// files returns:slave node absolute path
			// /usr/local/hadoop/hadoop-userid/tmp/mapred/local/taskTracker/distance/~
			if (files == null || files.length == 0) {
				throw new RuntimeException(
				"User information is not set in DistributedCache");
			}

			// 이 로직에서 클로저를 쓸경우 userIdToReputation의 값이 null로 들어오는 문제가 발생
			// 아마도 클로저 시점 문제인거 같은데 확인안됨
			//			files.each { p ->
			for (int i = 0; i < files.length; i++) {
				Path p = files[i]
				BufferedReader br = new BufferedReader(new InputStreamReader(
						new FileInputStream(p.toString())))

				String line
				String userId
				String reputation
				while ((line = br.readLine()) != null) {
					Map<String, String> parsed = MRDPUtils.transformXmlToMap(line)
					userId = parsed.get("Id")
					reputation = parsed.get("Reputation")
					println line
					if (userId != null && reputation != null) {
						// 사용자  ID에 평판을 매핑한다
						userIdToReputation.put(userId, reputation)
					}
				}
			}
		}

		@Override
		public void cleanup(Context context) {
			mos.close()
		}
	}

	static main(String[] args) {
		Configuration conf = new Configuration()
		if (args.length != 3) {
			println "Usage: JobChaining <posts> <users> <out>"
			System.exit(2)
		}
		Path postInput = new Path(args[0])
		Path userInput = new Path(args[1])
		Path outputDirIntermediate = new Path(args[2] + "_int")
		Path outputDir = new Path(args[2])

		FileSystem.get(conf).delete(outputDir, true)
		FileSystem.get(conf).delete(outputDirIntermediate, true)

		// 사용자 게시글 카운터를 위한 첫 번째 잡
		Job countingJob = new Job(conf, "JobChaining-Counting")
		countingJob.setJarByClass(JobChainingDriver.class)

		countingJob.setMapperClass(UserIdCountMapper.class)
		countingJob.setCombinerClass(LongSumReducer.class)
		countingJob.setReducerClass(UserIdSumReducer.class)

		countingJob.setOutputKeyClass(Text.class)
		countingJob.setOutputValueClass(LongWritable.class)

		countingJob.setInputFormatClass(TextInputFormat.class)
		TextInputFormat.addInputPath(countingJob, postInput)

		countingJob.setOutputFormatClass(TextOutputFormat.class)
		TextOutputFormat.setOutputPath(countingJob, outputDirIntermediate)

		int code = countingJob.waitForCompletion(true) ? 0 : 1
		if (code == 0) {
			println "************************************"

			double numRecords = (double) countingJob.getCounters()
					.findCounter(AVERAGE_CALC_GROUP,
					UserIdCountMapper.RECORDS_COUNTER_NAME).getValue()
			double numUsers = (double) countingJob.getCounters()
					.findCounter(AVERAGE_CALC_GROUP,
					UserIdSumReducer.USERS_COUNTER_NAME).getValue()

			// 유저당 평균 게시글 수
			double averagePostsPerUser = numRecords / numUsers

			Job binningJob = new Job(new Configuration(), "JobChaing-binning")
			binningJob.setJarByClass(JobChainingDriver.class)

			binningJob.setMapperClass(UserIdBinningMapper.class)
			UserIdBinningMapper.setAveragePostsPerUser(binningJob,
					averagePostsPerUser)

			binningJob.setNumReduceTasks(0)

			binningJob.setInputFormatClass(TextInputFormat.class)
			TextInputFormat.addInputPath(binningJob, outputDirIntermediate)

			// 평균 이상/이하를 위한 두 개의 NamedOutput 메서드를 추가
			MultipleOutputs.addNamedOutput(binningJob,
					MULTIPLE_OUTPUTS_BELOW_NAME, TextOutputFormat.class,
					Text.class, Text.class)
			MultipleOutputs.addNamedOutput(binningJob,
					MULTIPLE_OUTPUTS_ABOVE_NAME, TextOutputFormat.class,
					Text.class, Text.class)
			MultipleOutputs.setCountersEnabled(binningJob, true)

			TextOutputFormat.setOutputPath(binningJob, outputDir)

			FileStatus[] userFiles = FileSystem.get(conf).listStatus(userInput)
			// 이 로직에서 클로저를 쓸경우 userIdToReputation의 값이 null로 들어오는 문제가 발생
			// 아마도 클로저 시점 문제인거 같은데 확인안됨
			for (int i = 0; i < userFiles.length; i++) {
				FileStatus status = userFiles[i]
				//			userFiles.each { status ->
				// toUri returns : hdfs://master:9000/hdfs root~
				println "URI :" + status.getPath().toUri()
				DistributedCache.addCacheFile(status.getPath().toUri(),
						binningJob.getConfiguration())
			}
			println "userFiles:" + userFiles.length

			code = binningJob.waitForCompletion(true) ? 0 : 1
		}

		System.exit(code)
	}
}