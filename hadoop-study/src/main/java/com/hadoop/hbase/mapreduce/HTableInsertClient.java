package com.hadoop.hbase.mapreduce;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.NullOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * hdfs 파일을 읽어 hbase에 저장하는 코드
 * 
 * @author cuckoo03
 *
 */
public class HTableInsertClient extends Configured implements Tool {
	public static class InsertMapper extends
			Mapper<LongWritable, Text, LongWritable, Text> {
		private HTable table;

		@Override
		public void setup(Context context) throws IOException {
			Configuration conf = HBaseConfiguration.create();
			table = new HTable(conf, "mytable");
		}

		@Override
		public void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			byte[] rowkey = Bytes.toBytes(String.valueOf(key.get()));
			Put p = new Put(rowkey);
			p.add(Bytes.toBytes("cf"), Bytes.toBytes("data"),
					Bytes.toBytes(value.toString()));
			table.put(p);
		}

		@Override
		public void cleanup(Context context) throws IOException {
			table.close();
		}
	}

	@Override
	public int run(String[] args) throws Exception {
		if (args.length != 1) {
			System.err.println("Usage:HTableInsertClient <input>");
			return -1;
		}

		String input = args[0];
		Configuration conf = new Configuration();
		Job job = new Job(conf, "HTableInsertClient");

		job.setJarByClass(HTableInsertClient.class);
		job.setMapperClass(InsertMapper.class);

		job.setMapOutputKeyClass(LongWritable.class);
		job.setMapOutputValueClass(Text.class);

		job.setOutputFormatClass(NullOutputFormat.class);

		FileInputFormat.addInputPath(job, new Path(input));

		job.waitForCompletion(true);

		return 0;
	}

	public static void main(String[] args) throws Exception {
		int exitCode = ToolRunner.run(new HTableInsertClient(), args);
		System.exit(exitCode);
	}
}