package com.storm.cookbook.ch01.click;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jmock.Expectations;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import redis.clients.jedis.Jedis;
import scala.actors.threadpool.Arrays;
import backtype.storm.task.OutputCollector;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.storm.cookbook.ch01.click.bolt.RepeatVisitBolt;
import com.storm.cookbook.ch01.click.constant.FieldsConstant;

@RunWith(value = Parameterized.class)
public class TestRepeatVisitBolt extends StormTestCase {
	private String ip;
	private String clientKey;
	private String url;
	private String expected;

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] {
				{ "192.168.1.101", "client1", "localhost.com", "false" },
				{ "192.168.1.101", "client2", "localhost.com", "true" } };
		return Arrays.asList(data);
	}

	public TestRepeatVisitBolt(String ip, String clientKey, String url,
			String expected) {
		this.ip = ip;
		this.clientKey = clientKey;
		this.url = url;
		this.expected = expected;
	}

	@BeforeClass
	public static void setupJedis() {
		Jedis jedis = new Jedis("192.168.1.101", 6300);
		jedis.auth("12341234");
		jedis.flushDB();
		Iterator<Object[]> it = data().iterator();
		while (it.hasNext()) {
			Object[] values = it.next();
			if (values[3].equals("false")) {
				String key = values[2] + ":" + values[1];
				jedis.set(key, "visited");
				jedis.rpush("count", key);
			}
		}

	}

	@Test
	public void testExecute() {
		Jedis jedis = new Jedis("192.168.1.101", 6300);
		jedis.auth("12341234");
		RepeatVisitBolt bolt = new RepeatVisitBolt();
		Map<String, String> config = new HashMap<>();
		config.put("redis-host", "192.168.1.101");
		config.put("redis-port", "6300");
		config.put("redis-pass", "12341234");
		final OutputCollector collector = context.mock(OutputCollector.class);
		bolt.prepare(config, null, (OutputCollector) collector);

		assertEquals(true, bolt.isConnected());

		final Tuple tuple = getTuple();
		context.checking(new Expectations() {
			{
				allowing(tuple).getStringByField(FieldsConstant.CLIENT_KEY);
				will(returnValue(clientKey));
				one(tuple).getStringByField(FieldsConstant.IP);
				will(returnValue(ip));
				one(tuple).getStringByField(FieldsConstant.URL);
				will(returnValue(url));
				one(collector).emit(new Values(clientKey, url, expected));
			}
		});
		bolt.execute(tuple);
		context.assertIsSatisfied();

		if (jedis != null) {
			jedis.disconnect();
		}
		System.out.println(tuple.getStringByField(FieldsConstant.CLIENT_KEY));
	}
}