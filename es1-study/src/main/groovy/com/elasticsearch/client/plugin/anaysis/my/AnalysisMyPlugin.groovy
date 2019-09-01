package com.elasticsearch.client.plugin.anaysis.my

import org.elasticsearch.index.analysis.AnalysisModule
import org.elasticsearch.plugins.AbstractPlugin
import com.elasticsearch.client.index.analysis.MyAnalysisBinderProcessor
import groovy.transform.TypeChecked

@TypeChecked
class AnalysisMyPlugin extends AbstractPlugin {

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
