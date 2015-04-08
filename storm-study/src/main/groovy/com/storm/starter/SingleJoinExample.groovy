package com.storm.starter

import groovy.transform.TypeChecked
import backtype.storm.Config
import backtype.storm.LocalCluster
import backtype.storm.testing.FeederSpout
import backtype.storm.topology.TopologyBuilder
import backtype.storm.tuple.Fields
import backtype.storm.tuple.Values
import backtype.storm.utils.Utils

import com.storm.starter.bolt.SingleJoinBolt
import com.storm.starter.bolt.SingleJoinPrintBolt

@TypeChecked
class SingleJoinExample {
	static main(args) {
		FeederSpout genderSpout = new FeederSpout(new Fields("gender", "id"))
		FeederSpout ageSpout = new FeederSpout(new Fields("id", "id2"))

		TopologyBuilder builder = new TopologyBuilder()
		builder.setSpout("gender", genderSpout)
		builder.setSpout("age", ageSpout)
		builder.setBolt("join",
				new SingleJoinBolt(/*다음 스트림에 전달할 핃드명*/new Fields("gender", "id", "id2"))).
				fieldsGrouping("gender", /*조인할 필드*/new Fields("id")).
				fieldsGrouping("age", /*조인할 필드*/new Fields("id2"))
		builder.setBolt("print", new SingleJoinPrintBolt()).
				globalGrouping("join")

		Config conf = new Config()

		LocalCluster cluster = new LocalCluster()
		cluster.submitTopology("join-example", conf, builder.createTopology())

		for (int i = 0; i < 3; i++) {
			String gender
			if (i % 2 == 0) {
				gender = "male"
			} else {
				gender = "female"
			}
			println "gender:$gender, id:$i"
			genderSpout.feed(new Values(gender, i))
		}

		for (int i = 4; i >= 1; i--) {
			println "age:" + 2 + ", id2:" + i
			ageSpout.feed(new Values(i, i))
		}

		Utils.sleep(2000000)
		cluster.shutdown()
	}
}