package com.storm.gettingstarted;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.utils.Utils;

public class HelloTopologyLocal {
	public static void main(String[] args) {
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("HelloSpout", new HelloSpout(), 2);
		builder.setBolt("HelloBolt", new HelloBolt(), 4).shuffleGrouping(
				"HelloSpout");

		Config conf = new Config();
		conf.setDebug(true);
		LocalCluster cluster = new LocalCluster();

		cluster.submitTopology("HelloTopologyLocal", conf,
				builder.createTopology());
		Utils.sleep(5000);
		
		cluster.killTopology("HelloTopologyLocal");
		
		cluster.shutdown();
	}
}
