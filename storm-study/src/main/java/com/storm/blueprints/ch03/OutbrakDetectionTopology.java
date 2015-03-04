package com.storm.blueprints.ch03;

import storm.trident.Stream;
import storm.trident.TridentTopology;
import storm.trident.operation.builtin.Count;
import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.generated.StormTopology;
import backtype.storm.tuple.Fields;
import backtype.storm.utils.Utils;

import com.storm.blueprints.ch03.filter.DiseaseFilter;
import com.storm.blueprints.ch03.function.CityAssignment;
import com.storm.blueprints.ch03.function.DispatchAlert;
import com.storm.blueprints.ch03.function.HourAssignment;
import com.storm.blueprints.ch03.function.OutbreakDetector;
import com.storm.blueprints.ch03.spout.DiagnosisEventSpout;
import com.storm.blueprints.ch03.state.OutbreakTreandFactory;

public class OutbrakDetectionTopology {
	public static StormTopology buildTopology() {
		TridentTopology topology = new TridentTopology();
		DiagnosisEventSpout spout = new DiagnosisEventSpout();
		Stream inputStream = topology.newStream("event", spout);

		// 중요 이벤트 필터링
		inputStream.each(new Fields("event"), new DiseaseFilter());
		// 가장 근접도시 찾기
		inputStream.each(new Fields("event"), new CityAssignment(), new Fields(
				"city"))
		// 시간대 찾기
		.each(new Fields("event", "city"), new HourAssignment(),
				new Fields("hour", "cityDiseaseHour"))
		// 도시와 시간별로 발생수를 그룹핑
		.groupBy(new Fields("cityDiseaseHour"))
		// 발생횟수를 집계하고 결과를 저장
		.persistentAggregate(new OutbreakTreandFactory(),
				new Count(), new Fields("count")).newValuesStream()
		// 질병 발생을 감지
		.each(new Fields("cityDiseaseHour", "count"),
				new OutbreakDetector(), new Fields("alert"))
		// 알림 내보냄
		.each(new Fields("alert"), new DispatchAlert(), new Fields());
		return topology.build();
	}

	public static void main(String[] args) {
		Config conf = new Config();
		LocalCluster cluster = new LocalCluster();
		cluster.submitTopology("cdc", conf, buildTopology());
		Utils.sleep(100000);
		cluster.shutdown();
	}
}