package com.storm.hadoop;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import com.storm.hadoop.spout.WordSpout;

public class WordCountClient {
	public static void main(String[] args) throws IOException {
		Configuration conf = HBaseConfiguration.create();
		conf.clear();
		conf.set("hbase.rootdir", "hdfs://master:9000/hbase");
		conf.set("hbase.zookeeper.quorum", "master");

		HTable table = new HTable(conf, "word_count");

		for (String word : WordSpout.words) {
			Get get = new Get(Bytes.toBytes(word));
			Result result = table.get(get);

			byte[] countBytes = result.getValue(Bytes.toBytes("cf"),
					Bytes.toBytes("count"));
			byte[] wordBytes = result.getValue(Bytes.toBytes("cf"), 
					Bytes.toBytes("word"));
			
			String wordStr = Bytes.toString(wordBytes);
			long count = Bytes.toLong(countBytes);
			System.out.println(wordStr + "," + count);
		}

		table.close();
	}
}