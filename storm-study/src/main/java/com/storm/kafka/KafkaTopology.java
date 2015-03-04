package com.storm.kafka;

import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;

/**
 * kafka 메시지 받는 클라이언트
 * @author cuckoo03
 *
 */
public class KafkaTopology {
	public static void main(String[] args) {
		ZkHosts zkHosts = new ZkHosts("master:2181");
		SpoutConfig kafkaConfig = new SpoutConfig(zkHosts, "kafkatopic", "",
				"testgroup");
		kafkaConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
//		 kafkaConfig.forceFromStart = true;
		// 메시지를 처음부터 모두 받게 하는 옵션
		// 이 옵션이 없으면 동작한 이후부터 온 메시지만 받는다

		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("KafkaSpout", new KafkaSpout(kafkaConfig), 1);
		builder.setBolt("SentenceBolt", new SentenceBolt(), 1).globalGrouping(
				"KafkaSpout");
		builder.setBolt("PrintBolt", new PrintBolt(), 1).globalGrouping(
				"SentenceBolt");

		if (args.length == 0) {
			LocalCluster cluster = new LocalCluster();
			Config conf = new Config();

			cluster.submitTopology("KafkaTopology", conf,
					builder.createTopology());

			try {
				System.out.println("Waiting to consume from kafka");
				Thread.sleep(1000000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			cluster.killTopology("KafkaTopology");
			cluster.shutdown();
		} else {

		}
	}
}