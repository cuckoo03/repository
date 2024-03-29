package com.elasticsearch.plugin.analysis.my;

import org.elasticsearch.index.analysis.AnalysisModule;
import org.elasticsearch.plugins.AbstractPlugin;

import com.elasticsearch.index.analysis.MyAnalysisBinderProcessor;
import com.elasticsearch.index.analysis.OccasionAnalysisBinderProcessor;
import com.elasticsearch.index.analysis.SentimentAnalysisBinderProcessor;
import com.elasticsearch.index.analysis.TopicAnalysisBinderProcessor;

public class AnalysisMyPlugin extends AbstractPlugin {

	public String name() {
		return "analysis-my";
	}

	public String description() {
		return "My Analyzer";
	}
	public void onModule(AnalysisModule module) {
		module.addProcessor(new MyAnalysisBinderProcessor());
		module.addProcessor(new TopicAnalysisBinderProcessor());
		module.addProcessor(new SentimentAnalysisBinderProcessor());
		module.addProcessor(new OccasionAnalysisBinderProcessor());
	}
}