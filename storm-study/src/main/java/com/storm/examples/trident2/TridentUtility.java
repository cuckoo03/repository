package com.storm.examples.trident2;

import storm.trident.operation.BaseFilter;
import storm.trident.operation.BaseFunction;
import storm.trident.operation.TridentCollector;
import storm.trident.tuple.TridentTuple;
import backtype.storm.tuple.Values;

public class TridentUtility {
	public static class Split extends BaseFunction {
		private static final long serialVersionUID = 1L;

		@Override
		public void execute(TridentTuple tuple, TridentCollector collector) {
			String countries = tuple.getString(0);
			for (String word : countries.split(",")) {
				collector.emit(new Values(word));
			}
		}
	}

	public static class TweetFilter extends BaseFilter {
		private static final long serialVersionUID = 1L;

		@Override
		public boolean isKeep(TridentTuple tuple) {
			if (tuple.getString(0).contains("#FIFA")) {
				return true;
			} else {
				return false;
			}
		}
	}

	public static class Print extends BaseFilter {
		private static final long serialVersionUID = 1L;

		@Override
		public boolean isKeep(TridentTuple tuple) {
			System.out.println(tuple);
			return true;
		}
	}
}