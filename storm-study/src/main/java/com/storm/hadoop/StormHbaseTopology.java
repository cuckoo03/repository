package com.storm.hadoop;

import java.util.HashMap;
import java.util.Map;

import org.apache.storm.hbase.bolt.HBaseBolt;
import org.apache.storm.hbase.bolt.mapper.SimpleHBaseMapper;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import backtype.storm.utils.Utils;

import com.storm.hadoop.bolt.WordCounter;
import com.storm.hadoop.spout.WordSpout;

public class StormHbaseTopology {
	public static void main(String[] args) throws AlreadyAliveException,
			InvalidTopologyException {
		Config conf = new Config();

		Map<String, Object> hbConf = new HashMap<>();
		hbConf.put("hbase.rootdir", "hdfs://master:9000/hbase");
		hbConf.put("hbase.zookeeper.quorum", "master");
		conf.put("hbase.conf", hbConf);

		WordSpout spout = new WordSpout();
		WordCounter bolt = new WordCounter();

		SimpleHBaseMapper mapper = new SimpleHBaseMapper()
				.withRowKeyField("word").withColumnFields(new Fields("word"))
				.withCounterFields(new Fields("count")).withColumnFamily("cf");

		HBaseBolt hbase = new HBaseBolt("word_count", mapper)
				.withConfigKey("hbase.conf");

		TopologyBuilder builder = new TopologyBuilder();

		builder.setSpout("word-spout", spout);
		builder.setBolt("count-bolt", bolt).shuffleGrouping("word-spout");
//		builder.setBolt("hbase-bolt", hbase).fieldsGrouping("count-bolt",
//				new Fields("word"));

		if (args.length == 0) {
			LocalCluster local = new LocalCluster();
			local.submitTopology("hbase-topology", conf,
					builder.createTopology());
			Utils.sleep(20000);
			local.shutdown();
		} else {
			StormSubmitter.submitTopology("hbase-topology", conf,
					builder.createTopology());
		}
	}
}
