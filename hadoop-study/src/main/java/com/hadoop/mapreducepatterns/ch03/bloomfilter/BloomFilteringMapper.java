package com.hadoop.mapreducepatterns.ch03.bloomfilter;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.util.bloom.BloomFilter;
import org.apache.hadoop.util.bloom.Key;

import com.hadoop.mapreducepatterns.MRDPUtils;

public class BloomFilteringMapper extends
		Mapper<LongWritable, Text, Text, NullWritable> {
	private BloomFilter filter = new BloomFilter();

	@Override
	protected void setup(Context context) throws IOException {
		System.out.println("*********************");
		System.out.println("*********************");

		URI[] files = DistributedCache
				.getCacheFiles(context.getConfiguration());
		System.out.println("*************************");
		System.out.println("Reading bloom filter from uri: " + files);
		System.out.println("*************************");

		DataInputStream dis = new DataInputStream(new FileInputStream(
				files[0].getPath()));

		// suedo distributed mode
		// Path[] localFiles =
		// DistributedCache.getLocalCacheFiles(context.getConfiguration())
		// println "*****************"
		// println "localFiles[0]:" + localFiles[0]
		// println "*****************"
		// DataInputStream dis = new DataInputStream(
		// new FileInputStream(localFiles[0].toString()))

		// 블룸 필터로 읽음
		filter.readFields(dis);
		dis.close();
	}

	@Override
	public void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {

		Map<String, String> parsed = MRDPUtils.transformXmlToMap(value
				.toString());

		String comment = parsed.get("Text");
		StringTokenizer token = new StringTokenizer(comment);
		while (token.hasMoreTokens()) {
			String word = token.nextToken();
			// 코멘트의 각 단어에 대해 단어가 필터에 있다면 레코드를 출력하고 빠져나감.
			if (filter.membershipTest(new Key(word.getBytes()))) {
				context.write(value, NullWritable.get());
				break;
			}
		}

	}
}