package com.hadoop.mapreducepatterns.ch07.redis;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import redis.clients.jedis.Jedis;

/**
 * CSV 포맷의 레디스 인스턴스 목록이 주어질 때 설정된 해시에 저장된 모든 데이터를 병렬로 읽는다.
 * 
 * @author cuckoo03
 *
 */
public class RedisInputDriver {
	/**
	 * 개별 매퍼에서 처리되는 데이터를 기술한다.
	 * 
	 * @author cuckoo03
	 *
	 */
	public static class RedisHashInputSplit extends InputSplit implements
			Writable {
		private String location = null;
		private String hashKey = null;
		private String password = null;

		// 리플렉션을 위한 기본 생성자
		public RedisHashInputSplit() {
		}

		public RedisHashInputSplit(String redisHost, String hash,
				String password) {
			this.location = redisHost;
			this.hashKey = hash;
			this.password = password;
		}

		@Override
		public long getLength() throws IOException, InterruptedException {
			return 0;
		}

		@Override
		public String[] getLocations() throws IOException, InterruptedException {
			return new String[] { location };
		}

		@Override
		public void readFields(DataInput in) throws IOException {
			this.location = in.readUTF();
			this.hashKey = in.readUTF();
			this.password = in.readUTF();
		}

		@Override
		public void write(DataOutput out) throws IOException {
			out.writeUTF(location);
			out.writeUTF(hashKey);
			out.writeUTF(password);
		}

		public String getHashKey() {
			return hashKey;
		}

		public String getPassword() {
			return password;
		}
	}

	/**
	 * 어떤 레디스 인스턴스에 연결해야 하는지와 어던 해시에서 읽어야 하는지 알 수 있는 구성 변수가 들어있다.
	 * 
	 * @author cuckoo03
	 *
	 */
	public static class RedisHashInputFormat extends InputFormat<Text, Text> {
		public static final String REDIS_HOSTS_CONF = "mapred.redishashinputformat.hosts";
		public static final String REDIS_HASH_KEY_CONF = "mapred.redishashinputformat.key";
		public static final String REDIS_PASSWORD_CONF = "mapred.redishashinputformat.password";

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
		public RecordReader<Text, Text> createRecordReader(InputSplit split,
				TaskAttemptContext context) throws IOException,
				InterruptedException {
			return new RedisHashRecordReader();
		}

		@Override
		public List<InputSplit> getSplits(JobContext job) throws IOException,
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

			List<InputSplit> splits = new ArrayList<>();
			for (String host : hosts.split(",")) {
				splits.add(new RedisHashInputSplit(host, hashKey, password));
			}

			return splits;
		}
	}

	public static class RedisHashRecordReader extends RecordReader<Text, Text> {
		private Iterator<Entry<String, String>> keyValueMapIter = null;
		private Text key = new Text();
		private Text value = new Text();
		private float processedKVs = 0;
		private float totalKVs = 0;
		private Entry<String, String> currentEntry = null;

		@Override
		public void close() throws IOException {
		}

		@Override
		public Text getCurrentKey() throws IOException, InterruptedException {
			return key;
		}

		@Override
		public Text getCurrentValue() throws IOException, InterruptedException {
			return value;
		}

		@Override
		public float getProgress() throws IOException, InterruptedException {
			return processedKVs / totalKVs;
		}

		@Override
		public void initialize(InputSplit split, TaskAttemptContext context)
				throws IOException, InterruptedException {
			// 입력 스프릿에서 호스트 위치를 가져온다.
			String host = split.getLocations()[0];
			String hashKey = ((RedisHashInputSplit) split).getHashKey();
			String password = ((RedisHashInputSplit) split).getPassword();

			Jedis jedis = new Jedis(host.split(":")[0], Integer.parseInt(host
					.split(":")[1]));
			jedis.auth(password);
			jedis.connect();
			jedis.getClient().setTimeoutInfinite();

			// 레디스 인스턴스에서 모든 키/값 을 가져오고 이를 메모리에 저장한다.
			totalKVs = jedis.hlen(hashKey);
			keyValueMapIter = jedis.hgetAll(hashKey).entrySet().iterator();
			jedis.disconnect();
		}

		@Override
		public boolean nextKeyValue() throws IOException, InterruptedException {
			// 키/값이 존재하는 경우 해당 항목을 Text 객체에 설정
			if (keyValueMapIter.hasNext()) {
				currentEntry = keyValueMapIter.next();
				key.set(currentEntry.getKey());
				value.set(currentEntry.getValue());
				return true;
			} else {
				// 더이상 값이 없으면 false
				return false;
			}
		}
	}

	public static void main(String[] args) throws IOException,
			ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args)
				.getRemainingArgs();
		if (otherArgs.length != 4) {
			System.err
					.println("Usage:RedisInput <hosts:port,host:port> <password> <hash> <out>");
			System.exit(2);
		}

		Job job = new Job(conf, "Redis Input");
		job.setJarByClass(RedisInputDriver.class);

		String hosts = otherArgs[0];
		String password = otherArgs[1];
		String hashKey = otherArgs[2];
		Path outputDir = new Path(otherArgs[3]);

		job.setNumReduceTasks(0);

		job.setInputFormatClass(RedisHashInputFormat.class);
		RedisHashInputFormat.setRedisHosts(job, hosts);
		RedisHashInputFormat.setRedisPassword(job, password);
		RedisHashInputFormat.setRedisHashKey(job, hashKey);

		job.setOutputFormatClass(TextOutputFormat.class);
		TextOutputFormat.setOutputPath(job, outputDir);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		FileSystem.get(conf).delete(outputDir, true);

		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}