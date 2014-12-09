package com.hadoop.mapreducepatterns.ch02.minmaxcount;

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
import com.hadoop.mapreducepatterns.ch02.minmaxcount.MinMaxCountMapper
import com.hadoop.mapreducepatterns.ch02.minmaxcount.MinMaxCountReduce
import com.hadoop.mapreducepatterns.ch02.minmaxcount.MinMaxCountTuple

public class MinMaxCountDriverTest {
	private MapDriver<Object, Text, Text, MinMaxCountTuple> mapDriver;
	private ReduceDriver<Text, MinMaxCountTuple, Text, MinMaxCountTuple> reduceDriver;
	private MapReduceDriver<LongWritable, Text, Text, LongWritable, Text, LongWritable> mapReduceDriver;

	private final static SimpleDateFormat sdf = new SimpleDateFormat(
	"yyyy-MM-dd'T'HH:mm:ss.SSS");

	@Before
	public void setUp() {
		MinMaxCountMapper m = new MinMaxCountMapper();
		MinMaxCountReduce r = new MinMaxCountReduce();
		mapDriver = new MapDriver<>(m);
		reduceDriver = new ReduceDriver<>(r);
	}

	@Test
	@Ignore
	public void testMapper() throws IOException, ParseException {
		InputStream is = MRDPUtilsTest.class.getClassLoader()
				.getResourceAsStream("ubuntu/data/comments.xml");
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line = null;
		if ((line = br.readLine()) != null) {
			Map<String, String> parsed = MRDPUtils.transformXmlToMap(line);
			MinMaxCountTuple tuple = new MinMaxCountTuple();
			String strDate = parsed.get("CreationDate");
			Date creationDate = sdf.parse(strDate);
			tuple.setMax(creationDate);
			tuple.setMin(creationDate);
			tuple.setCount(1);
			String userId = parsed.get("UserId");

			mapDriver.withInput(new LongWritable(), new Text(line));
			mapDriver.withOutput(new Text(userId), tuple);
			mapDriver.runTest();
		}
	}

	@Test
	public void testReduce() throws IOException, ParseException {
		List<MinMaxCountTuple> list = new ArrayList<MinMaxCountTuple>();
		MinMaxCountTuple tuple = new MinMaxCountTuple();
		tuple.setMin(sdf.parse("2010-07-28T20:52:11.217"));
		tuple.setMax(sdf.parse("2010-07-28T20:52:11.217"));
		tuple.setCount(1);
		list.add(tuple);

		reduceDriver.withInput(new Text("1"), list);
		reduceDriver.withOutput(new Text("1"), tuple);
	}
}