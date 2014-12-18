package com.hadoop.mapreducepatterns.groovy.ch06.jobcontrol

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.FileStatus
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable
import org.apache.hadoop.io.Text
import org.apache.hadoop.mapreduce.Job
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat
import org.apache.hadoop.mapreduce.lib.jobcontrol.ControlledJob
import org.apache.hadoop.mapreduce.lib.jobcontrol.JobControl
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs
import org.apache.hadoop.mapreduce.lib.output.NullOutputFormat
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat
import org.apache.hadoop.mapreduce.lib.reduce.LongSumReducer
import org.apache.hadoop.util.GenericOptionsParser

import com.hadoop.mapreducepatterns.groovy.ch06.jobchain.JobChainingDriver
import com.hadoop.mapreducepatterns.groovy.ch06.jobchain.JobChainingDriver.UserIdBinningMapper
import com.hadoop.mapreducepatterns.groovy.ch06.jobchain.JobChainingDriver.UserIdCountMapper
import com.hadoop.mapreducepatterns.groovy.ch06.jobchain.JobChainingDriver.UserIdSumReducer
import com.hadoop.mapreducepatterns.groovy.ch06.parallel.ParallelJobDriver;
import com.hadoop.mapreducepatterns.groovy.ch06.parallel.ParallelJobDriver.AverageReputationMapper;
import com.hadoop.mapreducepatterns.groovy.ch06.parallel.ParallelJobDriver.AverageReputationReducer;

/**
 * 기본 잡 체인과 병렬 잡 체인을 조합한다
 * @author cuckoo03
 *
 */
class JobControlDriver {
	static main(args) {
		Configuration conf = new Configuration()

		String[] otherArgs = new GenericOptionsParser(conf, args)
				.getRemainingArgs()
		if (otherArgs.length != 4) {
			println "Usage:JobControl <posts> <users> <belowOutputDir> <aboveOutputDir>"
			System.exit(2)
		}
		Path postInput = new Path(otherArgs[0])
		Path userInput = new Path(otherArgs[1])
		Path countingOutput = new Path(otherArgs[3] + "_count")
		Path binningOutputRoot = new Path(otherArgs[3] + "bins")
		Path binningOutputBelow = new Path(binningOutputRoot.toString() + "/" + JobChainingDriver.MULTIPLE_OUTPUTS_BELOW_NAME)
		Path binningOutputAbove = new Path(binningOutputRoot.toString() + "/" +JobChainingDriver.MULTIPLE_OUTPUTS_ABOVE_NAME)

		FileSystem.get(conf).delete(countingOutput, true)
		FileSystem.get(conf).delete(binningOutputRoot, true)
		FileSystem.get(conf).delete(binningOutputBelow, true)
		FileSystem.get(conf).delete(binningOutputAbove, true)

		Path belowAverageRepOutput = new Path(otherArgs[2])
		Path aboveAverageRepOutput = new Path(otherArgs[3])

		FileSystem.get(conf).delete(belowAverageRepOutput, true)
		FileSystem.get(conf).delete(aboveAverageRepOutput, true)
		
		Job countingJob = getCountingJob(conf, postInput, countingOutput)

		int code = 1
		if (countingJob.waitForCompletion(true)) {
			ControlledJob binningControlledJob = new ControlledJob(
					getBinningJobConf(countingJob, conf, countingOutput,
					userInput, binningOutputRoot))

			ControlledJob belowAvgControlledJob = new ControlledJob(
					getAverageJobConf(conf, binningOutputBelow,
					belowAverageRepOutput))
			belowAvgControlledJob.addDependingJob(binningControlledJob)

			ControlledJob aboveAvgControlledJob = new ControlledJob(
					getAverageJobConf(conf, binningOutputAbove,
					aboveAverageRepOutput))
			aboveAvgControlledJob.addDependingJob(binningControlledJob)

			JobControl jc = new JobControl("AverageReputation")
			jc.addJob(binningControlledJob)
			jc.addJob(belowAvgControlledJob)
			jc.addJob(aboveAvgControlledJob)

			new Thread(jc).start()
			while (!jc.allFinished()) {
				println "Still running."
				sleep(3000)
			}
			println "job finished."
			
			code = jc.getFailedJobList().size() == 0 ? 0 : 1
		}

		FileSystem fs = FileSystem.get(conf)
//		fs.delete(countingOutput, true)
//		fs.delete(binningOutputRoot, true)

		System.exit(code)
	}

	private static Job getCountingJob(Configuration conf, Path input,
			Path output) {
		Job countingJob = new Job(conf, "JobChaining-Counting")
		countingJob.setJarByClass(JobChainingDriver.class)

		countingJob.setMapperClass(UserIdCountMapper.class)
		countingJob.setCombinerClass(LongSumReducer.class)
		countingJob.setReducerClass(UserIdSumReducer.class)

		countingJob.setOutputKeyClass(Text.class)
		countingJob.setOutputValueClass(LongWritable.class)

		countingJob.setInputFormatClass(TextInputFormat.class)
		TextInputFormat.addInputPath(countingJob, input)

		countingJob.setOutputFormatClass(TextOutputFormat.class)
		TextOutputFormat.setOutputPath(countingJob, output)

		return countingJob
	}

	private static Configuration getBinningJobConf(Job countingJob,
			Configuration conf, Path jobchainOutdir, Path userInput,
			Path binningOutput) {
			// 카운터 값을 가지고 사용자당 평균 게시글 수를 계산한다
		double numRecords = (double) countingJob.getCounters()
				.findCounter(JobChainingDriver.AVERAGE_CALC_GROUP,
				UserIdCountMapper.RECORDS_COUNTER_NAME).getValue()
		double numUsers = (double) countingJob.getCounters()
				.findCounter(JobChainingDriver.AVERAGE_CALC_GROUP,
				UserIdSumReducer.USERS_COUNTER_NAME).getValue()

		double averagePostsPerUser = numRecords / numUsers

		Job binningJob = new Job(conf, "JobChaining-Binning")
		binningJob.setJarByClass(JobChainingDriver.class)

		binningJob.setMapperClass(UserIdBinningMapper.class)
		UserIdBinningMapper.setAveragePostsPerUser(binningJob,
				averagePostsPerUser)

		binningJob.setNumReduceTasks(0)

		binningJob.setInputFormatClass(TextInputFormat.class)
		TextInputFormat.addInputPath(binningJob, jobchainOutdir)

		MultipleOutputs.addNamedOutput(binningJob,
				JobChainingDriver.MULTIPLE_OUTPUTS_BELOW_NAME,
				TextOutputFormat.class, Text.class, Text.class)

		MultipleOutputs.addNamedOutput(binningJob,
				JobChainingDriver.MULTIPLE_OUTPUTS_ABOVE_NAME,
				TextOutputFormat.class, Text.class, Text.class)
		
		MultipleOutputs.setCountersEnabled(binningJob, true)

		binningJob.setOutputFormatClass(TextOutputFormat.class)
		TextOutputFormat.setOutputPath(binningJob, binningOutput)

		FileStatus[] userFiles = FileSystem.get(conf).listStatus(userInput)
		for (FileStatus status : userFiles) {
			DistributedCache.addCacheFile(status.getPath().toUri(),
					binningJob.getConfiguration())
					println "URI : " + status.getPath().toUri() 
		}

		return binningJob.getConfiguration()
	}

	private static Configuration getAverageJobConf(Configuration conf,
			Path averageOutputDir, Path outputDir) {
		Job averageJob = new Job(conf, "ParallelJobs")
		averageJob.setJarByClass(ParallelJobDriver.class)

		averageJob.setMapperClass(AverageReputationMapper.class)
		averageJob.setReducerClass(AverageReputationReducer.class)

		averageJob.setOutputKeyClass(Text.class)
		averageJob.setOutputValueClass(DoubleWritable.class)

		averageJob.setInputFormatClass(TextInputFormat.class)
		TextInputFormat.addInputPath(averageJob, averageOutputDir)

		averageJob.setOutputFormatClass(TextOutputFormat.class)
		TextOutputFormat.setOutputPath(averageJob, outputDir)

		return averageJob.getConfiguration()
	}
}