package com.elasticsearch.plugin.analysis.my;

import org.elasticsearch.index.analysis.AnalysisModule;
import org.elasticsearch.plugins.AbstractPlugin;

import com.elasticsearch.index.analysis.MyAnalysisBinderProcessor;

public class AnalysisMyPlugin extends AbstractPlugin {

	public String name() {
		return "analysis-my";
	}

	public String description() {
		return "My Analyzer";
	}
	public void onModule(AnalysisModule module) {
		module.addProcessor(new MyAnalysisBinderProcessor());
	}
}