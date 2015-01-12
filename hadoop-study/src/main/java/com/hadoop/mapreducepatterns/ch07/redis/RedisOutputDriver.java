package com.hadoop.mapreducepatterns.ch07.redis;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.OutputCommitter;
import org.apache.hadoop.mapreduce.OutputFormat;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.NullOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;

import com.hadoop.doithadoop.ch04.WordCount;
import com.hadoop.mapreducepatterns.MRDPUtils;

/**
 * 사용자 정보 셋이 주어질 때 설정된 개수만큼 레디스 인스턴스에 사용자-평판 매핑을 병렬로 무작위 분배한다.
 * 
 * @author cuckoo03
 *
 */
public class RedisOutputDriver {
	/**
	 * 잡을 잡트래커에 제출하기 이전에 잡의 구성을 설정하고 검증할 책임이 있다.
	 * 
	 * @author cuckoo03
	 *
	 */
	public static class RedisHashOutputFormat extends OutputFormat<Text, Text> {
		public static final String REDIS_HOSTS_CONF = "mapred.redishashoutputformat.hosts";
		public static final String REDIS_PASSWORD_CONF = "mapred.redishashoutputformat.password";
		public static final String REDIS_HASH_KEY_CONF = "mapred.redishashinputformat.key";

		public static void setRedisHosts(Job job, String hosts) {
			job.getConfiguration().set(REDIS_HOSTS_CONF, hosts);
		}

		public static void setRedisHashKey(Job job, String hashKey) {
			job.getConfiguration().set(REDIS_HASH_KEY_CONF, hashKey);
		}

		public static void setRedisPassword(Job job, String password) {
			job.getConfiguration().set(REDIS_PASSWORD_CONF, password);
		}

		@Override
		public void checkOutputSpecs(JobContext job) throws IOException,
				InterruptedException {
			String hosts = job.getConfiguration().get(REDIS_HOSTS_CONF);
			if (hosts == null || hosts.isEmpty()) {
				throw new IOException(REDIS_HOSTS_CONF
						+ " is not set in configuration");
			}

			String hashKey = job.getConfiguration().get(REDIS_HASH_KEY_CONF);
			if (hashKey == null || hashKey.isEmpty()) {
				throw new IOException(REDIS_HASH_KEY_CONF
						+ " is not set in configuration");
			}
			String password = job.getConfiguration().get(REDIS_PASSWORD_CONF);
			if (password == null || password.isEmpty()) {
				throw new IOException(REDIS_PASSWORD_CONF
						+ " is not set in configuration");
			}
		}

		@Override
		public OutputCommitter getOutputCommitter(TaskAttemptContext context)
				throws IOException, InterruptedException {
			return (new NullOutputFormat<Text, Text>()
					.getOutputCommitter(context));
		}

		@Override
		public RecordWriter<Text, Text> getRecordWriter(TaskAttemptContext job)
				throws IOException, InterruptedException {
			return new RedisHashRecordWriter(job.getConfiguration().get(
					REDIS_HASH_KEY_CONF), job.getConfiguration().get(
					REDIS_HOSTS_CONF), job.getConfiguration().get(
					REDIS_PASSWORD_CONF));
		}
	}

	/**
	 * 제디스 클라이언트를 통해 레디스 연결을 처리하고 데이터를 출력한다
	 * 
	 * @author cuckoo03
	 *
	 */
	public static class RedisHashRecordWriter extends RecordWriter<Text, Text> {
		private static final Logger log = Logger.getLogger(WordCount.class
				.getName());
		private Map<Integer, Jedis> jedisMap = new HashMap<Integer, Jedis>();
		private String hashKey = null;

		public RedisHashRecordWriter(String hashKey, String hosts,
				String password) {
			this.hashKey = hashKey;
			log.warn("hosts:" + hosts + ", hashKey:" + hashKey);
			// 각 호스트를 레디스에 연결
			int i = 0;
			String[] split = null;
			for (String host : hosts.split(",")) {
				split = host.split(":");
				Jedis jedis = new Jedis(split[0], Integer.parseInt(split[1]));
				jedis.auth(password);
				jedis.connect();
				jedisMap.put(i, jedis);
				++i;
			}
		}

		@Override
		public void close(TaskAttemptContext context) throws IOException,
				InterruptedException {
			for (Jedis jedis : jedisMap.values()) {
				jedis.disconnect();
			}
		}

		@Override
		public void write(Text key, Text value) throws IOException,
				InterruptedException {
			// 키/값 쌍을 기록할 제디스 인스턴스를 가져옴
			Jedis j = jedisMap.get(Math.abs(key.hashCode()) % jedisMap.size());

			j.hset(hashKey, key.toString(), value.toString());
		}
	}

	/**
	 * 사용자ID와 평판을 레코드에서 찾아 출력한다
	 * 
	 * @author cuckoo03
	 *
	 */
	public static class RedisOutputMapper extends
			Mapper<LongWritable, Text, Text, Text> {
		private Text outkey = new Text();
		private Text outvalue = new Text();

		@Override
		public void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			Map<String, String> parsed = MRDPUtils.transformXmlToMap(value
					.toString());
			String userId = parsed.get("Id");
			String reputation = parsed.get("Reputation");
			if (userId == null || reputation == null) {
				return;
			}

			outkey.set(userId);
			outvalue.set(reputation);

			context.write(outkey, outvalue);
		}
	}

	public static void main(String[] args) throws IOException,
			ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args)
				.getRemainingArgs();
		if (otherArgs.length != 4) {
			System.err
					.println("Usage: RedisOutput <users> <host:port,host:port> <hashName> <password>");
			System.exit(2);
		}
		Path inputPath = new Path(otherArgs[0]);
		String hosts = otherArgs[1];
		String hashKey = otherArgs[2];
		String password = otherArgs[3];

		Job job = new Job(conf, "Redis Output");
		job.setJarByClass(RedisOutputDriver.class);

		job.setMapperClass(RedisOutputMapper.class);
		job.setNumReduceTasks(0);

		job.setInputFormatClass(TextInputFormat.class);
		TextInputFormat.addInputPath(job, inputPath);

		job.setOutputFormatClass(RedisHashOutputFormat.class);
		RedisHashOutputFormat.setRedisHosts(job, hosts);
		RedisHashOutputFormat.setRedisHashKey(job, hashKey);
		RedisHashOutputFormat.setRedisPassword(job, password);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}