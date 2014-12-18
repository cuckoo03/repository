package com.hadoop.mapreducepatterns.groovy.ch05.replicatedjoin

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.filecache.DistributedCache
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.LongWritable
import org.apache.hadoop.io.Text
import org.apache.hadoop.mapreduce.Job
import org.apache.hadoop.mapreduce.Mapper
import org.apache.hadoop.mapreduce.Mapper.Context
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat
import org.apache.hadoop.util.GenericOptionsParser

import com.hadoop.mapreducepatterns.MRDPUtils
import com.jcraft.jsch.UserInfo;

/**
 * 사용자 정보의 작은 데이터 셋과 코멘트의 큰 데이터 셋이 주어질 때 사용자 정보를 코멘트에 추가한다
 * output
 * commentId:1, userId:10	userId:10
 * commentId:2, userId:28	userId:28
 * commentId:3, userId:5	userId:5
 * commentId:4, userId:12	userId:12
 * commentId:5, userId:4	userId:4
 * @author cuckoo03
 *
 */
class ReplicatedJoinDriver {
	public static class ReplicatedJoinMapper extends Mapper<LongWritable, Text,
	Text, Text> {
		private static final Text EMPTY_TEXT = new Text("")
		private Map<String, String> userIdToInfo = new HashMap<>()
		private Text outvalue = new Text()
		private String joinType = null

		@Override
		public void setup(Context context) {
			try {
				Path[] files = DistributedCache.getLocalCacheFiles(context
						.getConfiguration());
				// files returns:slave node absolute path
				// /usr/local/hadoop/hadoop-userid/tmp/mapred/local/taskTracker/distance/~
				if (files == null || files.length == 0) {
					throw new RuntimeException(
					"User information is not set in DistributedCache");
				}

				// Read all files in the DistributedCache
				for (Path p : files) {
					BufferedReader rdr = new BufferedReader(
							new InputStreamReader(new FileInputStream(
							new File(p.toString()))));
					String line;
					// For each record in the user file
					while ((line = rdr.readLine()) != null) {
						// Get the user ID for this record
						Map<String, String> parsed = MRDPUtils
								.transformXmlToMap(line);
						String userId = parsed.get("Id");
						// 사용자 ID와 레코드로 맵 자료 구조 구성
//						userIdToInfo.put(userId, line)
						userIdToInfo.put(userId, "userId:$userId")

						joinType = context.getConfiguration().get("join.type")
					}
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		@Override
		public void map(LongWritable key, Text value, Context context) {
			// comment records
			Map<String, String> parsed = MRDPUtils.transformXmlToMap(
					value.toString())
			String userId = parsed.get("UserId")
			String userInformation = userIdToInfo.get(userId)
			String commentId = parsed.get("Id")

			if (userInformation != null) {
				outvalue.set(userInformation)
//				context.write(value, outvalue)
				context.write("commentId:$commentId, userId:$userId", outvalue)
			} else if (joinType.toString().equalsIgnoreCase("leftouter")) {
				context.write(value, EMPTY_TEXT)
			}
		}
	}

	static main(args) {
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args)
				.getRemainingArgs();
		if (otherArgs.length != 4) {
			System.err
					.println("Usage: ReplicatedJoin <user data> <comment data> <out> [inner|leftouter]");
			System.exit(2);
		}
		String joinType = otherArgs[3];
		if (!(joinType.equalsIgnoreCase("inner")
		|| joinType.equalsIgnoreCase("leftouter"))) {
			System.err.println("Join type not set to inner or leftouter");
			System.exit(2);
		}
		// Configure the join type
		Job job = new Job(conf, "Replicated Join");
		job.getConfiguration().set("join.type", joinType);
		job.setJarByClass(ReplicatedJoinDriver.class);

		job.setMapperClass(ReplicatedJoinMapper.class);
		job.setNumReduceTasks(0);
		TextInputFormat.setInputPaths(job, new Path(otherArgs[1]));
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		Path outputDir = new Path(otherArgs[2])
		TextOutputFormat.setOutputPath(job, outputDir);
		FileSystem.get(conf).delete(outputDir, true)
		
		// toUri() returns:hdfs file path(/hdfs root/filepath~)
		DistributedCache.addCacheFile(new Path(otherArgs[0]).toUri(),
				job.getConfiguration())

		System.exit(job.waitForCompletion(true) ? 0 : 3);
	}
}