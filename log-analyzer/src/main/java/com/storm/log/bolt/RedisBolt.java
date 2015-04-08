package com.storm.log.bolt;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

import com.spring.RedisTemplateBean;

public class RedisBolt extends BaseRichBolt {
	private static final long serialVersionUID = 1L;
	@Autowired
	private RedisTemplateBean template;

	public RedisBolt() {
	}

	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		ApplicationContext appContext = new GenericXmlApplicationContext(
				"classpath:spring/application-context.xml");
		template = appContext.getBean("redisTemplateBean",
				RedisTemplateBean.class);
	}

	@Override
	public void execute(Tuple input) {
		String line = input.getStringByField("line");
		template.getValueOps().set("log", line);
		System.out.println(template.getValueOps().get("log"));

	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {

	}
	@Override
	public void cleanup() {
		
	}
}