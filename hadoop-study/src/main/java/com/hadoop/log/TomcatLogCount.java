package com.hadoop.log;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.log4j.Logger;

/**
 * 톰캣 로그 데이터를 hbase에 넣고 시간 기준으로 카운트 세기
 * 
 * @author cuckoo03
 *
 */
public class TomcatLogCount {
	public static class LogCountMapper extends
			Mapper<LongWritable, Text, LongWritable, LogTuple> {
		private static final DateFormat sdf = new SimpleDateFormat(
				"dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);
		private static final Logger log = org.apache.log4j.Logger
				.getLogger(TomcatLogCount.class.getName());
		private static Pattern p = Pattern.compile(getAccessLogRegex());

		private HTable table;
		private byte[] columnFamily = Bytes.toBytes("cf");
		private byte[] localhostCol = Bytes.toBytes("localhost");
		private byte[] identdCol = Bytes.toBytes("identd");
		private byte[] userIdCol = Bytes.toBytes("user_id");
		private byte[] requestInfoCol = Bytes.toBytes("request_info");
		private byte[] requestDateCol = Bytes.toBytes("request_date");
		private byte[] statusCol = Bytes.toBytes("status");
		private byte[] sizeCol = Bytes.toBytes("size");

		private LongWritable keyText = new LongWritable();
		private LogTuple valueOne = new LogTuple();
		private int invalidCount = 0;

		private enum RECORDS_COUNTER {
			INVALID
		};

		@Override
		public void setup(Context context) throws IOException {
			Configuration conf = HBaseConfiguration.create();
			table = new HTable(conf, "apache_log");
		}

		@Override
		public void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			String strValue = value.toString();

			Matcher matcher = p.matcher(strValue);
			if (!matcher.matches()) {
				return;
			}
			try {
				valueOne.setLocalhost(matcher.group(1));
				valueOne.setIdentd(matcher.group(2));
				valueOne.setUserId(matcher.group(3));
				valueOne.setRequestDate(sdf.parse(matcher.group(4)));
				valueOne.setRequestInfo(matcher.group(5));
				valueOne.setStatus(Integer.parseInt(matcher.group(6)));
				valueOne.setSize(matcher.group(7));

				keyText.set(valueOne.getRequestDate().getTime());

				// put row to table
				Put put = new Put(Bytes.toBytes(String.valueOf(keyText.get())));
				put.add(columnFamily, localhostCol,
						Bytes.toBytes(valueOne.getLocalhost()));
				put.add(columnFamily, identdCol,
						Bytes.toBytes(valueOne.getIdentd()));
				put.add(columnFamily, userIdCol,
						Bytes.toBytes(valueOne.getUserId()));
				put.add(columnFamily, requestInfoCol,
						Bytes.toBytes(valueOne.getRequestInfo()));
				put.add(columnFamily, requestDateCol,
						Bytes.toBytes(valueOne.getFormattedRequestDate()));
				put.add(columnFamily, statusCol,
						Bytes.toBytes(String.valueOf(valueOne.getStatus())));
				put.add(columnFamily, sizeCol,
						Bytes.toBytes(valueOne.getSize()));

				table.put(put);

				context.write(keyText, valueOne);
			} catch (ParseException e) {
				e.printStackTrace();
				context.getCounter(RECORDS_COUNTER.INVALID).increment(1);
				processError(context, key, value);
			}
		}

		@Override
		public void cleanup(Context context) throws IOException {
			table.close();
		}

		static String getAccessLogRegex() {
			String regex1 = "^([\\d.]+)"; // Client IP (처음에 숫자형식(123.) 이 여러번 나오는
											// 것에 match)
			String regex2 = " (\\S+)"; // - (공백 아님)
			String regex3 = " (\\S+)"; // - (공백 아님)
			String regex4 = " \\[([\\w:/]+\\s[+\\-]\\d{4})\\]";
			// time
			// String regex4 = " \\[([\\w:/]+)";// 
			// timezone
			// String regex5 = "\\s([+\\-]\\d{4})\\]";// timezone
			String regex6 = " \"(.+?)\""; // request method and url :
											// 따옴표(") ~ 따옴표(")
			String regex7 = " (\\d{3})"; // HTTP code : 숫자연속 3번
			String regex8 = " (\\d+|(.+?))"; // Number of bytes, -일 때도 있음.

			return regex1 + regex2 + regex3 + regex4 + regex6 + regex7 + regex8;
		}

		protected void processError(Context c, LongWritable k, Text v) {
			log.error("Caught exception processing :" + k + ", " + v);

			c.getCounter(RECORDS_COUNTER.INVALID).increment(1);

			c.setStatus("Failures:" + (++invalidCount));
		}
	}

	public static class LogCountReducer extends
			Reducer<LongWritable, LogTuple, LongWritable, LongWritable> {
		private LogTuple sumValue = new LogTuple();

		@Override
		public void reduce(LongWritable key, Iterable<LogTuple> values,
				Context context) throws IOException, InterruptedException {
			int sum = 0;
			for (LogTuple val : values) {
				System.out.println(val.toString());
				sum += 1;
			}
			context.write(key, new LongWritable(sum));
		}
	}

	public static void main(String[] args) throws IOException,
			ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args)
				.getRemainingArgs();

		if (otherArgs.length != 2) {
			System.err
					.println("Usage: TomcatLogCount <in dir filename> <outdir>");
			System.exit(1);
		}

		Job job = new Job(conf);

		job.setJarByClass(TomcatLogCount.class);
		job.setMapperClass(TomcatLogCount.LogCountMapper.class);
		job.setReducerClass(TomcatLogCount.LogCountReducer.class);

		job.setMapOutputKeyClass(LongWritable.class);
		job.setMapOutputValueClass(LogTuple.class);
		job.setOutputKeyClass(LongWritable.class);
		job.setOutputValueClass(LongWritable.class);

		TextInputFormat.addInputPath(job, new Path(otherArgs[0]));
		TextOutputFormat.setOutputPath(job, new Path(otherArgs[1]));

		FileSystem.get(conf).delete(new Path(otherArgs[1]), true);
		System.out.println(otherArgs[1] + " dir is deleted.");

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}