package com.storm.examples.madvirus.trident;

import storm.trident.TridentTopology;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.generated.StormTopology;
import backtype.storm.tuple.Fields;

public class LogTopology {
	public static void main(String[] args) {
		TridentTopology topology = new TridentTopology();
		topology.newStream("log", new LogSpout())
				// use filter
				.each(new Fields("logString"), new OrderLogFilter())
				// use basefunction
				// .each(new Fields("logString"), new LogParser(), new Fields("shopLog"))
				// shuffle
				.parallelismHint(1).shuffle()
				// use grouping
//				.groupBy(new Fields("logString"))
				// use aggregate
//				.aggregate(new Fields("logString"), new Count(),
//						new Fields("count"))
				.each(new Fields("logString"), new PrintFunction());
		StormTopology stormTopology = topology.build();

		Config conf = new Config();
		LocalCluster local = new LocalCluster();
		local.submitTopology("test", conf, stormTopology);

		try {
			Thread.sleep(100000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
