package com.hadoop.hbase.mapreduce;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.GenericOptionsParser;

/**
 * HBase 테이블의 행 수를 조회하는 맵리듀스 잡. 행 수는 Counter로 카운ㅌ, 데이터 출력 없음.
 * 
 * hbase>create 'sample', 'data' 
 * hbase>put 'sample', 'row1', 'data:column', 'val' 
 * hbase>put 'sample', 'row2', 'data:column', 'val' 
 * hbase>put 'sample', 'row2', 'data:column', 'val' 
 * hbase>create 'sample_uniq', 'data'
 * 
 * @author cuckoo03
 *
 */
public class HTableUniqValueCounter {
	/**
	 * Hbase테이블을 입력 데이터로 하는 매퍼 <key, value>가 전달되므로 <value, 1>을 출력하는 매퍼 key는 테이블의
	 * 행 키, values는 모든 열의 값 output
	 * 
	 * @author cuckoo03
	 *
	 */
	static class UniqValueCounterMapper extends
			TableMapper<ImmutableBytesWritable, IntWritable> {
		ImmutableBytesWritable key = new ImmutableBytesWritable();
		IntWritable one = new IntWritable(1);

		/**
		 * input
		 * [map]key:row0, value:row0/data:column/1423198860264/Put/vlen=4
		 * [map]key:row1, value:val1
		 * [map]key:row2, value:val2
		 */
		@Override
		public void map(ImmutableBytesWritable key, Result values,
				Context context) throws IOException, InterruptedException {
			for (KeyValue value : values.list()) {
				System.out.println("[map]key:" + Bytes.toString(key.get())
						+ ", value:" + Bytes.toString(value.getValue()));
				
				key.set(value.getValue());
				context.write(key, one);
			}
		
		}
	}

	/**
	 * hbase 테이블을 출력하는 리듀서 출력 value형은 put or delete여야 한다. value 출현 횟수를 sum한다.
	 * 
	 * @author cuckoo03
	 *
	 */
	static class UniqValueCounterReducer extends
			TableReducer<ImmutableBytesWritable, IntWritable, NullWritable> {
		byte[] family = Bytes.toBytes("data");
		byte[] column = Bytes.toBytes("column");

		/**
		 * [reduce]key:val, value1
		 * [reduce]key:val, value1
		 * [reduce]key:val0, value1
		 */
		@Override
		public void reduce(ImmutableBytesWritable row,
				Iterable<IntWritable> values, Context context)
				throws IOException, InterruptedException {
			int sum = 0;
			for (IntWritable value : values) {
				sum += value.get();
				System.out.println("[reduce]key:" + Bytes.toString(row.get())
						+ ", value" + value.get());
			}

			// put클래스를 작성해서 테이블에 출력하는 방법 지정
			Put put = new Put(row.get());
			put.add(family, column, Bytes.toBytes(Integer.toString(sum)));

			context.write(NullWritable.get(), put);
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
		job.setJarByClass(HTableUniqValueCounter.class);

		TableMapReduceUtil.initTableMapperJob(tableName, new Scan(),
				UniqValueCounterMapper.class, ImmutableBytesWritable.class,
				IntWritable.class, job);

		TableMapReduceUtil.initTableReducerJob("sample_uniq",
				UniqValueCounterReducer.class, job);

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}