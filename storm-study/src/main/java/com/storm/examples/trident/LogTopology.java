package com.storm.examples.trident;

import storm.trident.TridentTopology;
import backtype.storm.Config;
import backtype.storm.generated.StormTopology;
import backtype.storm.tuple.Fields;

public class LogTopology {
	public static void main(String[] args) {
		TridentTopology topology = new TridentTopology();
		topology.newStream("log", new LogSpout()).each(new Fields("logString"),
				new OrderLogFilter());
		StormTopology stormTopology = topology.build();
		
		Config conf = new Config();
	}
}
