package com.hadoop.mapreducepatterns.groovy.ch05.reduceside

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.io.LongWritable
import org.apache.hadoop.io.Text
import org.apache.hadoop.mapreduce.Job
import org.apache.hadoop.mapreduce.Mapper
import org.apache.hadoop.mapreduce.Reducer
import org.apache.hadoop.mapreduce.Mapper.Context
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat
import org.apache.hadoop.util.GenericOptionsParser

import com.hadoop.mapreducepatterns.MRDPUtils

/**
 * 사용자 정보 셋과 사용자 코멘트 목록이 주어질때 코멘트 작성자의 정보를 각 코멘트에 추가한다
 * @author cuckoo03
 *
 */
class ReduceSideJoinDriver {
	public static class UserJoinMapper extends Mapper<LongWritable, Text, Text,
	Text> {
		private Text outkey = new Text()
		private Text outvalue = new Text()
		@Override
		public void map(LongWritable key, Text value, Context context) {
			Map<String, String> parsed = MRDPUtils.transformXmlToMap(
					value.toString())
			String userId = parsed.get("Id")
			if (userId == null){
				return
			}
			//외래조인 키는 사용자 ID
			outkey.set(userId)

			// 리듀서를 위해 레코드에 태그를 표시한후 출력
			outvalue.set("A userId:$userId")
			context.write(outkey, outvalue)
		}
	}

	public static class CommentJoinMapper extends Mapper<LongWritable, Text,
	Text, Text>{
		private Text outkey = new Text()
		private Text outvalue = new Text()
		@Override
		public void map(LongWritable key, Text value, Context context) {
			Map<String, String> parsed = MRDPUtils.transformXmlToMap(
					value.toString())
			String userId = parsed.get("UserId")
			String commentId = parsed.get("Id")
			if (userId == null) {
				return
			}
			outkey.set(userId)

			outvalue.set("B" + "userid:$userId, commentid:$commentId")
			context.write(outkey, outvalue)
		}
	}

	public static class UserJoinReducer extends Reducer<Text, Text, Text, Text> {
		private static final Text EMPTY_TEXT = new Text("*****NULL*****")
		private List<Text> listA = new ArrayList<>()
		private List<Text> listB = new ArrayList<>()
		private String joinType = null
		@Override
		public void setup(org.apache.hadoop.mapreduce.Reducer.Context context) {
			joinType = context.getConfiguration().get("join.type")
		}
		@Override
		public void reduce(Text key, Iterable<Text> values,
				org.apache.hadoop.mapreduce.Reducer.Context context) {
			listA.clear()
			listB.clear()

			values.each { value->
				if (value.toString().charAt(0) == "A") {
					listA.add(new Text(value.toString().substring(1)))
				} else if (value.toString().charAt(0) == "B") {
					listB.add(new Text(value.toString().substring(1)))
				}
			}
			executeJoinLogic(context)
		}

		private void executeJoinLogic(
				org.apache.hadoop.mapreduce.Reducer.Context context) {
			// 두 리스트가 비어 있지 않으면 A,B를 조인
			if (joinType.equalsIgnoreCase("inner")) {
				if (!listA.isEmpty() && !listB.isEmpty()) {
					listA.each { A ->
						listB.each{ B ->
							context.write(A, B)
						}
					}
				}
			} else if (joinType.equalsIgnoreCase("leftouter")) {
				// A 항목에 대해
				listA.each { A->
					if (!listB.isEmpty()) {
						listB.each { B->
							context.write(A, B)
						}
					} else {
						context.write(A, EMPTY_TEXT)
					}
				}
			} else if (joinType.equalsIgnoreCase("rightouter")) {
				listB.each { B->
					if (!listA.isEmpty()) {
						listA.each { A->
							context.write(A, B)
						}
					} else {
						context.write(EMPTY_TEXT, B)
					}
				}
			} else if (joinType.equalsIgnoreCase("fullouter")) {
				if (!listA.isEmpty()) {
					listA.each { A ->
						if (!listB.isEmpty()) {
							listB.each {B->
								context.write(A, B)
							}
						} else {
							listB.each {B->
								context.write(EMPTY_TEXT, B)
							}
						}
					}
				}
			} else if (joinType.equalsIgnoreCase("anti")) {
				// 만약 A 또는 B 목록이 비어 있거나 그 반대라면
				if (listA.isEmpty() ^ listB.isEmpty()) {
					// 양쪽 A, B를 null 값으로 반복 처리
					// XOR검사는 이 목록 중 하나가 비어 있는 것을 확인하여 비어 있는 목록을 건너띈다
					listA.each { A->
						context.write(A, EMPTY_TEXT)
					}

					listB.each { B->
						context.write(EMPTY_TEXT, B)
					}
				}
			} else {
				throw new RuntimeException("Join type not set to inner, leftouter, rightouter, fullouter or anti")
			}
		}
	}

	static main(args) {
		Configuration conf = new Configuration()
		String[] otherArgs = new GenericOptionsParser(conf, args)
				.getRemainingArgs()
		if (otherArgs.length != 4) {
			println "Usage:ReduceSideJoinDriver <user data> <comment data> <out> [inner|leftouter|rightouter|fullouter|anti]"
			System.exit(2)
		}
		String joinType = otherArgs[3]
		if (!(joinType.equalsIgnoreCase("inner")
		|| joinType.equalsIgnoreCase("leftouter")
		|| joinType.equalsIgnoreCase("rightouter")
		|| joinType.equalsIgnoreCase("fullouter")
		|| joinType.equalsIgnoreCase("anti"))) {
			println "Join type not set to inner, leftouter, rightouter, fullouter or anti"
			System.exit(2)
		}

		Job job = new Job(conf, "Reduce Side Join")

		job.getConfiguration().set("join.type", joinType)
		job.setJarByClass(ReduceSideJoinDriver.class)

		MultipleInputs.addInputPath(job, new Path(otherArgs[0]),
				TextInputFormat.class, UserJoinMapper.class)
		MultipleInputs.addInputPath(job, new Path(otherArgs[1]),
				TextInputFormat.class, CommentJoinMapper.class)

		job.setReducerClass(UserJoinReducer.class)
		job.setOutputKeyClass(Text.class)
		job.setOutputValueClass(Text.class)

		Path outputDir = new Path(otherArgs[2])
		FileSystem.get(conf).delete(outputDir, true)
		FileOutputFormat.setOutputPath(job, outputDir)

		System.exit(job.waitForCompletion(true) ? 0 : 1)
	}
}