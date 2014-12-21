package com.hadoop.mapreducepatterns.ch07.random;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

/**
 * 실제로 데이터가 생성되는 곳
 * 
 * @author cuckoo03
 *
 */
public class RandomStackOverflowRecordReader extends
		RecordReader<Text, NullWritable> {
	private int numRecordsToCreate = 0;
	private int createdRecords = 0;
	private Text key = new Text();
	private NullWritable value = NullWritable.get();
	private Random random = new Random();
	private List<String> randomWords = new ArrayList<>();
	private SimpleDateFormat fmt = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss.SSS");

	@Override
	public void close() throws IOException {
	}

	@Override
	public Text getCurrentKey() throws IOException, InterruptedException {
		return key;
	}

	@Override
	public NullWritable getCurrentValue() throws IOException,
			InterruptedException {
		return value;
	}

	@Override
	public float getProgress() throws IOException, InterruptedException {
		return (float) createdRecords / (float) numRecordsToCreate;
	}

	@Override
	public void initialize(InputSplit split, TaskAttemptContext context)
			throws IOException, InterruptedException {
		// 설정에서 생성해야 하는 레코드 수를 가져온다
		this.numRecordsToCreate = context.getConfiguration().getInt(
				RandomStackOverflowInputFormat.NUM_RECORDS_PER_TASK, -1);

		if (numRecordsToCreate < 0) {
			throw new InvalidParameterException(
					RandomStackOverflowInputFormat.NUM_RECORDS_PER_TASK
							+ " is not set.");
		}

		// 랜덤한 단어 목록을 가져온다
		Path[] files = DistributedCache.getLocalCacheFiles(context
				.getConfiguration());

		if (files == null || files.length == 0) {
			throw new RuntimeException(
					"User information is not set in DistributedCache");
		}
		BufferedReader br = new BufferedReader(new FileReader(
				files[0].toString()));
		String line;
		while ((line = br.readLine()) != null) {
			randomWords.add(line);
		}
		br.close();

		if (randomWords.size() == 0) {
			throw new IOException("Random word list is empty");
		}
	}

	/**
	 * 랜덤 발생기를 사용하여 랜덤한 레코드가 생성된다 모든 레코드가 생성되면 레코드 리더는 false를 반환하고 매퍼로 들어갈 더 이상의
	 * 입력이 없다는 신호를 프레임워크에 보낸다
	 */
	@Override
	public boolean nextKeyValue() throws IOException, InterruptedException {
		// 만약 생성할 레코드가 있다면
		if (createdRecords < numRecordsToCreate) {
			int score = Math.abs(random.nextInt()) % 15000;
			int rowId = Math.abs(random.nextInt()) % 10000;
			int postId = Math.abs(random.nextInt()) % 10000;
			int userId = Math.abs(random.nextInt()) % 10000;
			String creationDate = fmt.format(Math.abs(random.nextLong()));

			String text = getRandomText();

			String randomRecord = "<row Id=\"" + rowId + "\" PostId=\""
					+ postId + "\" Score=\"" + score + "\" Text=\"" + text
					+ "\" CreationDate=\"" + creationDate + "\" UserId=\""
					+ userId + "\" />";
			key.set(randomRecord);
			++createdRecords;
			return true;
		} else {
			return false;
		}
	}

	private String getRandomText() {
		StringBuilder sb = new StringBuilder();
		int numWords = Math.abs(random.nextInt()) % 30 + 1;

		for (int i = 0; i < numWords; ++i) {
			sb.append(randomWords.get(Math.abs(random.nextInt())
					% randomWords.size())
					+ " ");
		}
		return sb.toString();
	}
}