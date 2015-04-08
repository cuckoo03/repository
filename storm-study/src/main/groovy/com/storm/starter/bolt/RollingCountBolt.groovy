package com.storm.starter.bolt

import groovy.transform.TypeChecked

import java.util.Map.Entry

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import storm.starter.tools.NthLastModifiedTimeTracker
import storm.starter.tools.SlidingWindowCounter
import storm.starter.util.TupleHelpers
import backtype.storm.Config
import backtype.storm.task.OutputCollector
import backtype.storm.task.TopologyContext
import backtype.storm.topology.OutputFieldsDeclarer
import backtype.storm.topology.base.BaseRichBolt
import backtype.storm.tuple.Fields
import backtype.storm.tuple.Tuple
import backtype.storm.tuple.Values
import backtype.storm.utils.Utils

import com.storm.starter.config.FieldConsistants

@TypeChecked
class RollingCountBolt extends BaseRichBolt {
	private static final Logger LOG = LoggerFactory.getLogger(RollingCountBolt.class)
	private static final int NUM_WINDOW_CHUNKS = 1
	// 주기적으로 슬라이딩 되는 시간
	private static final int DEFAULT_SLIDING_WINDOW_IN_SECONDS = NUM_WINDOW_CHUNKS * 60
	// 주기적으로 스트림에 emit되는 시간
	private static final int DEFAULT_EMIT_FREQUENCY_IN_SECONDS = (DEFAULT_SLIDING_WINDOW_IN_SECONDS / NUM_WINDOW_CHUNKS).intValue()
	private static final String WINDOW_LENGTH_WARNING_TEMPLATE = "window length warining"

	private final SlidingWindowCounter<Object> counter
	private final int windowLengthInSeconds
	private final int emitFrequencyInSeconds
	private OutputCollector collector
	private NthLastModifiedTimeTracker lastModifiedTracker

	public RollingCountBolt(){
		this(DEFAULT_SLIDING_WINDOW_IN_SECONDS,
		DEFAULT_EMIT_FREQUENCY_IN_SECONDS)
	}

	public RollingCountBolt(int windowLengthInSeconds,
	int emitFrequencyInSeconds) {
		this.windowLengthInSeconds = windowLengthInSeconds
		this.emitFrequencyInSeconds = emitFrequencyInSeconds
		counter = new SlidingWindowCounter<>(deriveNumWindowchunksFrom(
				this.windowLengthInSeconds, this.emitFrequencyInSeconds))
	}

	private int deriveNumWindowchunksFrom(int windowLengthInSeconds,
			int windowUpdateFrequencyInSeconds) {
		println "deriveNum:" + windowLengthInSeconds / windowUpdateFrequencyInSeconds
		return (int)windowLengthInSeconds / windowUpdateFrequencyInSeconds
	}

	@Override
	public void prepare(Map conf, TopologyContext context,
			OutputCollector collector) {
		this.collector = collector
		lastModifiedTracker = new NthLastModifiedTimeTracker(
				deriveNumWindowchunksFrom(windowLengthInSeconds,
				emitFrequencyInSeconds))
	}

	@Override
	public void execute(Tuple tuple) {
		if (TupleHelpers.isTickTuple(tuple)) {
			//			LOG.debug("Received tick tuple")
			println "isTickTuple and emit"
			emitCurrentWindowCounts()
		} else {
			countObjAndAck(tuple)
		}
	}

	private void emitCurrentWindowCounts() {
		Map<Object, Long> counts = counter.getCountsThenAdvanceWindow()

		println counts

		int actualWindowLengthInSeconds =
				lastModifiedTracker.secondsSinceOldestModification()
		lastModifiedTracker.markAsModified()
		if (actualWindowLengthInSeconds != windowLengthInSeconds) {
			LOG.warn(String.format(WINDOW_LENGTH_WARNING_TEMPLATE,
					actualWindowLengthInSeconds, windowLengthInSeconds))
		}
		emit(counts, actualWindowLengthInSeconds)
	}
	private void emit(Map<Object, Long> counts, int actualWindowLengthInSeconds){
		for (Entry<Object, Long> entry : counts.entrySet()) {
			Object obj = entry.getKey()
			Long count = entry.getValue()
			collector.emit(new Values(obj, count, actualWindowLengthInSeconds))
		}
	}

	private void countObjAndAck(Tuple tuple) {
		Object obj = tuple.getValue(0)
		counter.incrementCount(obj)
		collector.ack(tuple)
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields(FieldConsistants.OBJ,
				FieldConsistants.COUNT,
				FieldConsistants.ACTUAL_WINDOW_LENGTH_IN_SECONDS))
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		Map<String, Object> conf = new HashMap<>()
		conf.put(Config.TOPOLOGY_TICK_TUPLE_FREQ_SECS, emitFrequencyInSeconds)
		return conf
	}
}