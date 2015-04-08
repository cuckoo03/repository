package com.storm.starter.bolt


import groovy.transform.TypeChecked

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import storm.starter.tools.Rankable;
import storm.starter.tools.Rankings

@TypeChecked
class TotalRankingsBolt extends AbstractRankerBolt {
	private static final Logger LOG = LoggerFactory.getLogger(TotalRankingsBolt.class)
	public TotalRankingsBolt() {
		super()
	}
	public TotalRankingsBolt(int topN) {
		super(topN)
	}
	public TotalRankingsBolt(int topN, int emitFrequencyInSeconds) {
		super(topN, emitFrequencyInSeconds)
	}
	@Override
	void updateRankingsWithTuple(backtype.storm.tuple.Tuple tuple) {
		Rankings rankingsToBeMerged = (Rankings) tuple.getValue(0)
		super.getRankings().updateWith(rankingsToBeMerged)
		super.getRankings().pruneZeroCounts()
		rankingsToBeMerged.rerank()
		List<Rankable> l = rankingsToBeMerged.getRankings();
		for (Rankable r : l) {
			println r
		}
	}
}