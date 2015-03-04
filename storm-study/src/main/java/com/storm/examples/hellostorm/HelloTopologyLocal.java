package com.storm.examples.hellostorm;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import backtype.storm.utils.Utils;

public class HelloTopologyLocal {
	public static void main(String[] args) {
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("HelloSpout", new HelloSpout(), 1);
		builder.setBolt("HelloBolt", new HelloBolt(), 2).fieldsGrouping(
				"HelloSpout", new Fields("word")).setNumTasks(4);
		builder.setBolt("PrintBolt", new PrintBolt()).globalGrouping(
				"HelloBolt");

		Config conf = new Config();
		conf.setDebug(true);
		conf.setNumWorkers(1);
		LocalCluster cluster = new LocalCluster();

		cluster.submitTopology("HelloTopologyLocal", conf,
				builder.createTopology());
		Utils.sleep(10000);

		cluster.killTopology("HelloTopologyLocal");

		cluster.shutdown();
	}
}
