package com.storm.cookbook.ch01.click.spout;

import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;

import com.storm.common.Conf;
import com.storm.cookbook.ch01.click.constant.FieldsConstant;

public class ClickSpout extends BaseRichSpout {
	private static final long serialVersionUID = 1L;
	private static Logger LOG = LoggerFactory.getLogger(ClickSpout.class);
	private Jedis jedis;
	private String host;
	private int port;
	private String passwd;
	private SpoutOutputCollector collector;

	@Override
	public void open(Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		host = conf.get(Conf.REDIS_HOST_KEY).toString();
		port = Integer.parseInt(conf.get(Conf.REDIS_PORT_KEY).toString());
		passwd = conf.get(Conf.REDIS_PASS_KEY).toString();
		this.collector = collector;
		connectRedis();
	}

	/**
	 * jedis의 데이터는 json string으로 된 데이터 여야 한다. format:ip, url, client-key
	 */
	@Override
	public void nextTuple() {
		String content = jedis.rpop("count");
		if (content == null || "nil".equals(content)) {
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			JSONObject obj = (JSONObject) JSONValue.parse(content);
			String ip = obj.get(FieldsConstant.IP).toString();
			String url = obj.get(FieldsConstant.URL).toString();
			String clientKey = obj.get(FieldsConstant.CLIENT_KEY).toString();

			collector.emit(new Values(ip, url, clientKey));
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields(FieldsConstant.IP, FieldsConstant.URL,
				FieldsConstant.CLIENT_KEY));
	}

	private void connectRedis() {
		jedis = new Jedis(host, port);
		jedis.auth(passwd);
	}
}