package com.hadoop.mapreducepatterns.ch02.invertedindex;

import java.text.ParseException
import java.text.SimpleDateFormat

import org.apache.hadoop.io.LongWritable
import org.apache.hadoop.io.Text
import org.apache.hadoop.mrunit.mapreduce.MapDriver
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

import com.hadoop.mapreducepatterens.MRDPUtils
import com.hadoop.mapreducepatterns.MRDPUtilsTest
import com.hadoop.mapreducepatterns.groovy.ch02.minmaxcount.MinMaxCountTuple
import com.hadoop.mapreducepatterns.groovy.ch02.reversedIndex.WikipediaExtractor

public class WikipediaIndexDriverTest {
	private MapDriver<LongWritable, Text, Text, Text> mapDriver;
	private ReduceDriver<Text, MinMaxCountTuple, Text, MinMaxCountTuple> reduceDriver;
	private MapReduceDriver<LongWritable, Text, Text, LongWritable, Text, LongWritable> mapReduceDriver;

	private final static SimpleDateFormat sdf = new SimpleDateFormat(
	"yyyy-MM-dd'T'HH:mm:ss.SSS");

	@Before
	public void setUp() {
		WikipediaExtractor m = new WikipediaExtractor()
		mapDriver = new MapDriver<>(m);
	}

	@Test
	public void testMapper() throws IOException, ParseException {
		InputStream is = MRDPUtilsTest.class.getClassLoader()
				.getResourceAsStream("ubuntu/data/posts.xml");
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line = null;
		while ((line = br.readLine()) != null) {
			Map<String, String> parsed = MRDPUtils.transformXmlToMap(line);

			mapDriver.withInput(new LongWritable(1), new Text(line));
			mapDriver.withOutput(new Text(1), new Text(1))
			mapDriver.runTest();
		}
	}

	@Test
	public void testReduce() throws IOException, ParseException {
	}
}