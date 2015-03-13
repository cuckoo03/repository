package com.storm.cookbook.ch01.click.bolt;

import java.util.Map;

import redis.clients.jedis.Jedis;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.storm.common.Conf;
import com.storm.cookbook.ch01.click.constant.FieldsConstant;

public class RepeatVisitBolt extends BaseRichBolt {
	private static final long serialVersionUID = 1L;
	private OutputCollector collector;
	private Jedis jedis;
	private String host;
	private int port;
	private String passwd;

	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		this.collector = collector;
		host = stormConf.get(Conf.REDIS_HOST_KEY).toString();
		port = Integer.parseInt(stormConf.get(Conf.REDIS_PORT_KEY).toString());
		passwd = stormConf.get(Conf.REDIS_PASS_KEY).toString();

		connectRedis();
	}

	@Override
	public void execute(Tuple input) {
		String clientKey = input.getStringByField(FieldsConstant.CLIENT_KEY);
		String url = input.getStringByField(FieldsConstant.URL);
		String key = url + ":" + clientKey;
		String value = jedis.get(key);
		if (value == null) {
			jedis.set(key, "visited");
			collector
					.emit(new Values(clientKey, url, Boolean.TRUE.toString()));
		} else {
			collector.emit(new Values(clientKey, url, Boolean.FALSE.toString()));
		}
		System.out.println("RepeatVisitBolt:" + clientKey + "," + url);
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields(FieldsConstant.CLIENT_KEY,
				FieldsConstant.URL, FieldsConstant.UNIQUE));
	}

	private void connectRedis() {
		jedis = new Jedis(host, port);
		jedis.auth(passwd);
		jedis.connect();
	}

	public boolean isConnected() {
		if (jedis == null) {
			return false;
		}
		return jedis.isConnected();
	}
}