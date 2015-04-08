package com.storm.starter.bolt

import groovy.transform.TypeChecked

import org.slf4j.Logger
import org.slf4j.LoggerFactory;

import storm.starter.tools.Rankable
import storm.starter.tools.RankableObjectWithFields

@TypeChecked
final class IntermediateRankingBolt extends AbstractRankerBolt {
	private static final Logger LOG = LoggerFactory.getLogger(IntermediateRankingBolt.class)

	public IntermediateRankingBolt() {
		super()
	}
	public IntermediateRankingBolt(int topN) {
		super(topN)
	}
	public IntermediateRankingBolt(int topN, int emitFrequencyInSeconds) {
		super(topN, emitFrequencyInSeconds)
	}

	@Override
	void updateRankingsWithTuple(backtype.storm.tuple.Tuple tuple) {
		Rankable rankable = RankableObjectWithFields.from(tuple)
		super.getRankings().updateWith(rankable)
	}
}