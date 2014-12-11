package com.hadoop.mapreducepatterns.ch02.counter;

import java.text.ParseException
import java.text.SimpleDateFormat

import org.apache.hadoop.io.LongWritable
import org.apache.hadoop.io.NullWritable
import org.apache.hadoop.io.Text
import org.apache.hadoop.mrunit.mapreduce.MapDriver
import org.junit.Before
import org.junit.Test

import com.hadoop.mapreducepatterns.MRDPUtils
import com.hadoop.mapreducepatterns.MRDPUtilsTest
import com.hadoop.mapreducepatterns.groovy.ch03.counter.CountNumUsersByStateMapper

public class CountNumUsersByStateDriverTest {
	private MapDriver<LongWritable, Text, NullWritable, NullWritable> mapDriver;

	private final static SimpleDateFormat sdf = new SimpleDateFormat(
	"yyyy-MM-dd'T'HH:mm:ss.SSS");

	@Before
	public void setUp() {
		CountNumUsersByStateMapper m = new CountNumUsersByStateMapper()
		mapDriver = new MapDriver<>(m);
	}

	@Test
	public void testMapper() throws IOException, ParseException {
		InputStream is = MRDPUtilsTest.class.getClassLoader()
				.getResourceAsStream("ubuntu/data/users.xml");
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line = null;
		while ((line = br.readLine()) != null) {
			Map<String, String> parsed = MRDPUtils.transformXmlToMap(line);

			mapDriver.withInput(new LongWritable(1), new Text(line));
			mapDriver.withOutput(new NullWritable(), new NullWritable())
			mapDriver.runTest();
		}
	}

	@Test
	public void testReduce() throws IOException, ParseException {
	}
}