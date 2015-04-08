package com.storm.starter.bolt

import java.util.List;
import java.util.Map;

import storm.trident.fluent.GlobalAggregationScheme;
import backtype.storm.Config
import backtype.storm.generated.GlobalStreamId
import backtype.storm.task.OutputCollector
import backtype.storm.task.TopologyContext
import backtype.storm.topology.OutputFieldsDeclarer
import backtype.storm.topology.base.BaseRichBolt
import backtype.storm.tuple.Fields
import backtype.storm.tuple.Tuple
import backtype.storm.utils.TimeCacheMap

@SuppressWarnings("deprecation")
class SingleJoinBolt extends BaseRichBolt {
	private OutputCollector collector
	private Fields idFields
	// delare에 전달할 값 필드
	private Fields outFields
	private int numSources
	private TimeCacheMap<List<Object>, Map<GlobalStreamId, Tuple>> pending
	private Map<String, GlobalStreamId> fieldLocations

	public SingleJoinBolt(Fields outFields) {
		this.outFields = outFields
	}

	@Override
	public void prepare(Map conf, TopologyContext context,
			OutputCollector collector) {
		fieldLocations = new HashMap<>()
		this.collector = collector
		int timeout = ((Number)
				conf.get(Config.TOPOLOGY_MESSAGE_TIMEOUT_SECS)).intValue()
		pending = new TimeCacheMap<>(timeout, new ExpireCallback())
		// 핃드 그룹핑 컴포넌트의 개수
		numSources = context.getThisSources().size()
		Set<String> idFieldsSet = null
		// 볼트에 설정된 필드 중에서 조인할 동일한 이름의 필드와 스트림 객체를 찾아낸다.
		for (GlobalStreamId source : context.getThisSources().keySet()) {
			Fields fields = context.getComponentOutputFields(
					source.get_componentId(), source.get_streamId())
			Set<String> setFields = new HashSet<String>(fields.toList())
			if (idFieldsSet == null) {
				idFieldsSet = setFields
			} else {
				idFieldsSet.retainAll(setFields)
			}

			println "fields:$fields"
			for (String outField : outFields){
				for (String sourceField : fields) {
					if (outField.equals(sourceField)) {
						fieldLocations.put(outField, source)
						println "outField:$outField, source:$source"
					}
				}
			}
		}
		idFields = new Fields(new ArrayList<String>(idFieldsSet))

		if (fieldLocations.size() != outFields.size()) {
			throw new RuntimeException("cannot find all outfiles among sources")
			println "RuntimeException"
		}
	}

	@Override
	public void execute(Tuple tuple) {
		List<Object> id = tuple.select(idFields)
		GlobalStreamId streamId = new GlobalStreamId(
				tuple.getSourceComponent(), tuple.getSourceStreamId())
		if (!pending.containsKey(id)) {
			pending.put(id, new HashMap<>())
		}
		//필드 그룹핑별 튜플리스트
		Map<GlobalStreamId, Tuple> parts = pending.get(id)
		if (parts.containsKey(streamId)) {
			throw new RuntimeException("received same side of single join twice")
		}
		parts.put(streamId, tuple)
		if (parts.size() == numSources) {
			pending.remove(id)
			// 조인된 튜플들의 값
			List<Object> joinResult = new ArrayList<Object>()
			for (String outField : outFields) {
				GlobalStreamId loc = fieldLocations.get(outField)
				joinResult.add(parts.get(loc).getValueByField(outField))
			}
			
			printTuple(new ArrayList<>(parts.values()))
//			printField(joinResult)
			// 조인할 튜플리스트, 조인된 튜플의 값을 가지고 있는 리스트
			collector.emit(new ArrayList<Tuple>(parts.values()), joinResult)

			for (Tuple part : parts.values()) {
				collector.ack(part)
			}
		}
	}

	private void printTuple(List list) {
		for (Object obj : list) {
			println obj
		}
	}

	private void printField(List list) {
		for (Object obj : list) {
			print obj + ", "
		}
		println ""
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(outFields)
	}

	private class ExpireCallback implements TimeCacheMap.ExpiredCallback<List<Object>, Map<GlobalStreamId, Tuple>> {
		@Override
		public void expire(List<Object> key, Map<GlobalStreamId, Tuple> tuples) {
			for (Tuple tuple : tuples.values()) {
				collector.fail(tuple)
			}
		}
	}
}