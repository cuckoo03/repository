package com.hadoop.hbase.mapreduce;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.FirstKeyOnlyFilter;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.RowCounter;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.NullOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

/**
 * HBase 테이블의 행 수를 조회하는 맵리듀스 잡
 * 행 수는 Counter로 카운ㅌ, 데이터 출력 없음.
 * @author cuckoo03
 *
 */
public class HTableRowCounter {
	static class RowCounterMapper extends
			TableMapper<ImmutableBytesWritable, Result> {
		public static enum Counters {
			ROWS
		}

	}

	public static void main(String[] args) throws IOException,
			ClassNotFoundException, InterruptedException {
		Configuration conf = HBaseConfiguration.create();
		conf.addResource("/usr/local/hbase/conf/hbase-default.xml");
		conf.addResource("/usr/local/hbase/conf/hbase-site.xml");

		new GenericOptionsParser(conf, args);

		String tableName = "sample";
		Job job = new Job(conf, "HBaseTableRowCounter_" + tableName);
		job.setJarByClass(HTableRowCounter.class);

		job.setOutputFormatClass(NullOutputFormat.class);
		job.setNumReduceTasks(0);

		Scan scan = new Scan();
		scan.setFilter(new FirstKeyOnlyFilter());

		TableMapReduceUtil.initTableMapperJob(tableName, scan,
				RowCounterMapper.class, ImmutableBytesWritable.class,
				Result.class, job);

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}