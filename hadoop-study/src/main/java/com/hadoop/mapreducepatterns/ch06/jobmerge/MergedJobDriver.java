package com.hadoop.mapreducepatterns.ch06.jobmerge;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import com.hadoop.mapreducepatterns.MRDPUtils;

/**
 * 코멘트 집합이 주어질 때 익명화된 데이터 버전과 사용자ID가 구별된 집합을 생성한다 anonymize output:<row
 * CreationDate="yyyy-MM-dd" Text="" PostId=""> distinct output:userId
 * 
 * @author cuckoo03
 *
 */
public class MergedJobDriver {
	public static final String MULTIPLE_OUTPUTS_ANONYMIZE = "anonymize";
	public static final String MULTIPLE_OUTPUTS_DISTINCT = "distinct";

	public static class AnonymizeDistinctMergeMapper extends
			Mapper<LongWritable, Text, TaggedText, Text> {
		private static final Text DISTINCT_OUT_VALUE = new Text();
		private Random random = new Random();
		private TaggedText anonymizeOutKey = new TaggedText();
		private TaggedText distinctOutkey = new TaggedText();
		private Text anonymizeOutValue = new Text();

		@Override
		public void map(LongWritable key, Text value, Context context) {
			try {
				anonymizeMap(key, value, context);
				distinctMap(key, value, context);
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}

		/**
		 * 레코드로부터 사용자ID를 얻어 이를 출력
		 * 
		 * @param key
		 * @param value
		 * @param context
		 * @throws IOException
		 * @throws InterruptedException
		 */
		private void distinctMap(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			Map<String, String> parsed = MRDPUtils.transformXmlToMap(value
					.toString());
			distinctOutkey.setTag("D");
			distinctOutkey.setText(parsed.get("UserId"));

			context.write(distinctOutkey, DISTINCT_OUT_VALUE);
		}

		/**
		 * 입력 값으로부터 익명 레코드를 생성
		 * 
		 * @param key
		 * @param value
		 * @param context
		 * @throws IOException
		 * @throws InterruptedException
		 */
		private void anonymizeMap(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			Map<String, String> parsed = MRDPUtils.transformXmlToMap(value
					.toString());
			if (parsed.size() > 0) {
				StringBuilder sb = new StringBuilder();
				sb.append("<row ");
				for (Entry<String, String> entry : parsed.entrySet()) {
					if (entry.getKey().equals("UserId")
							|| entry.getKey().equals("Id")) {
					} else if (entry.getKey().equals("CreationDate")) {
						// 값에서 시간을 제거
						sb.append(entry.getKey()
								+ "=\""
								+ entry.getValue().substring(0,
										entry.getValue().indexOf('T')) + "\" ");
					} else {
						sb.append(entry.getKey() + "=\"" + entry.getValue()
								+ "\" ");
					}
				}

				sb.append(">");
				anonymizeOutKey.setTag("A");
				anonymizeOutKey.setText(Integer.toString(random.nextInt()));
				anonymizeOutValue.set(sb.toString());
				context.write(anonymizeOutKey, anonymizeOutValue);
			}
		}
	}

	public static class AnonymizeDistinctMergedReducer extends
			Reducer<TaggedText, Text, Text, NullWritable> {
		private MultipleOutputs<Text, NullWritable> mos = null;

		@Override
		protected void setup(Context context) {
			mos = new MultipleOutputs<>(context);
		}

		/**
		 * key:TaggedText
		 * value:Null or row record
		 */
		@Override
		protected void reduce(TaggedText key, Iterable<Text> values,
				Context context) {
			try {
				if (key.getTag().equals("A")) {
					anonymizeReduce(key.getText(), values, context);
				} else {
					distinctReduce(key.getText(), values, context);
				}
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}

		@Override
		protected void cleanup(Context context) {
			try {
				mos.close();
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}

		private void distinctReduce(Text key, Iterable<Text> values,
				Context context) throws IOException, InterruptedException {
			// 입력 키와 값을 명명된 출력 디렉토리에 MultipleOutputs로 출력
			mos.write(MULTIPLE_OUTPUTS_DISTINCT, key, NullWritable.get(),
					MULTIPLE_OUTPUTS_DISTINCT + "/part");
		}

		private void anonymizeReduce(Text key, Iterable<Text> values,
				Context context) throws IOException, InterruptedException {
			for (Text value : values) {
				mos.write(MULTIPLE_OUTPUTS_ANONYMIZE, value,
						NullWritable.get(), MULTIPLE_OUTPUTS_ANONYMIZE
								+ "/part");
			}
		}
	}

	public static void main(String[] args) throws IOException,
			ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args)
				.getRemainingArgs();
		if (otherArgs.length != 2) {
			System.err.println("Usage MergedJob <comments> <out>");
			System.exit(2);
		}
		Job job = new Job(conf, "MergedJob");
		job.setJarByClass(MergedJobDriver.class);

		job.setMapperClass(AnonymizeDistinctMergeMapper.class);
		job.setReducerClass(AnonymizeDistinctMergedReducer.class);
		job.setNumReduceTasks(10);

		TextInputFormat.setInputPaths(job, new Path(otherArgs[0]));
		TextOutputFormat.setOutputPath(job, new Path(otherArgs[1]));

		FileSystem.get(conf).delete(new Path(otherArgs[1]), true);
		System.out.println(otherArgs[1] + " dir delete");

		MultipleOutputs.addNamedOutput(job, MULTIPLE_OUTPUTS_ANONYMIZE,
				TextOutputFormat.class, Text.class, NullWritable.class);
		MultipleOutputs.addNamedOutput(job, MULTIPLE_OUTPUTS_DISTINCT,
				TextOutputFormat.class, Text.class, NullWritable.class);

		job.setMapOutputKeyClass(TaggedText.class);
		job.setMapOutputValueClass(Text.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(NullWritable.class);

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}