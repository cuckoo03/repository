package com.storm.log;

import java.util.Properties;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import backtype.storm.utils.Utils;

import com.spring.ConfigProperty;
import com.spring.RedisTemplateBean;
import com.storm.common.StormClient;
import com.storm.log.bolt.CountBolt;
import com.storm.log.bolt.LogSplitBolt;
import com.storm.log.bolt.PrintBolt;
import com.storm.log.bolt.RedisBolt;

public class LogTopology implements StormClient {
	private static final String KAFKA_SPOUT_ID = "kafka-spout";
	private static final String SPLIT_BOLT_ID = "split-bolt";
	private static final String COUNT_BOLT_ID = "count-bolt";
	private static final String PRINT_BOLT_ID = "print-bolt";

	private static final String LOG_TOPOLOGY = "log-topology";

	private static ConfigProperty configProperty;

	private LocalCluster localCluster;
	private TopologyBuilder builder = new TopologyBuilder();
	private Config conf = new Config();

	public static void main(String[] args) throws AlreadyAliveException,
			InvalidTopologyException {
		ApplicationContext context = new GenericXmlApplicationContext(
				"classpath:spring/application-context.xml");
		configProperty = context
				.getBean("configProperty", ConfigProperty.class);

		LogTopology client = new LogTopology();
		client.createTopology();

		// localcluster
		if (args.length == 0) {
			client.runCluster();
		} else {// cluster
			client.runLocal(Integer.parseInt(args[0]));
		}
	}

	@Override
	public void createTopology() {
		ZkHosts zkHosts = new ZkHosts(configProperty.get("kafka.host"));
		SpoutConfig kafkaConfig = new SpoutConfig(zkHosts,
				configProperty.get("kafka.topic"), "",
				configProperty.get("kafka.group"));
		kafkaConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
		
		LogSplitBolt splitBolt = new LogSplitBolt();
		CountBolt countBolt = new CountBolt();
		PrintBolt printBolt = new PrintBolt();

		builder.setSpout(KAFKA_SPOUT_ID, new KafkaSpout(kafkaConfig));
		builder.setBolt(SPLIT_BOLT_ID, splitBolt).shuffleGrouping(
				KAFKA_SPOUT_ID);
		builder.setBolt(COUNT_BOLT_ID, countBolt).fieldsGrouping(SPLIT_BOLT_ID,
				new Fields("localhost"));
		builder.setBolt(PRINT_BOLT_ID, printBolt).globalGrouping(COUNT_BOLT_ID);
	}

	@Override
	public void runLocal(int runTime) {
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology(LOG_TOPOLOGY, conf, builder.createTopology());
		if (runTime > 0) {
			Utils.sleep(runTime);
			shutdownLocal();
		}
	}

	@Override
	public void shutdownLocal() {
		localCluster.killTopology(LOG_TOPOLOGY);
		localCluster.shutdown();
	}

	@Override
	public void runCluster() throws AlreadyAliveException,
			InvalidTopologyException {
		StormSubmitter.submitTopology(LOG_TOPOLOGY, conf,
				builder.createTopology());
	}
}