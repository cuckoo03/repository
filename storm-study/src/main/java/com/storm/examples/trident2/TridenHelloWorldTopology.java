package com.storm.examples.trident2;

import storm.trident.TridentTopology;
import storm.trident.operation.builtin.Count;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.generated.StormTopology;
import backtype.storm.tuple.Fields;

public class TridenHelloWorldTopology {
	public static void main(String[] args) throws AlreadyAliveException,
			InvalidTopologyException {
		Config conf = new Config();
		conf.setMaxSpoutPending(20);
		if (args.length == 0) {
			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology("Count", conf, buildTopology());
		} else {
			conf.setNumWorkers(1);
			StormSubmitter.submitTopology(args[0], conf, buildTopology());
		}
	}

	private static StormTopology buildTopology() {
		FakeTweetSpout spout = new FakeTweetSpout(10);
		TridentTopology topology = new TridentTopology();

		topology.newStream("faketweetspout", spout)
				.shuffle()
				.each(new Fields("text", "Country"),
						new TridentUtility.TweetFilter())
				.groupBy(new Fields("Country"))
				.aggregate(new Fields("Country"), new Count(),
						new Fields("count"))
				.each(new Fields("count"), new TridentUtility.Print())
				.parallelismHint(2);
		return topology.build();
	}
}