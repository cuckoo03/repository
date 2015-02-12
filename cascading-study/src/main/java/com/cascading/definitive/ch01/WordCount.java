package com.cascading.definitive.ch01;

import java.util.Properties;

import cascading.flow.Flow;
import cascading.flow.FlowConnector;
import cascading.flow.FlowDef;
import cascading.flow.hadoop.HadoopFlowConnector;
import cascading.operation.aggregator.Count;
import cascading.operation.regex.RegexSplitGenerator;
import cascading.pipe.Each;
import cascading.pipe.Every;
import cascading.pipe.GroupBy;
import cascading.pipe.Pipe;
import cascading.scheme.hadoop.TextDelimited;
import cascading.tap.Tap;
import cascading.tap.hadoop.Hfs;
import cascading.tuple.Fields;

public class WordCount {
	public static void main(String[] args) {
		String inPath = args[0];
		String outPath = args[1];

		Tap inTap = new Hfs(new TextDelimited(false, "\t"), inPath);
		Tap outTap = new Hfs(new TextDelimited(false, "\t"), outPath);

		Fields token = new Fields("token");
		Fields text = new Fields("text");
		RegexSplitGenerator splitter = new RegexSplitGenerator(token,
				"[ \\[\\]\\(\\),.]");
		Pipe docPipe = new Each("token", text, splitter, Fields.RESULTS);

		Pipe wcPipe = new Pipe("wc", docPipe);
		wcPipe = new GroupBy(wcPipe, token);
		wcPipe = new Every(wcPipe, Fields.ALL, new Count(), Fields.ALL);

		FlowDef flowDef = FlowDef.flowDef().setName("wc")
				.addSource(docPipe, inTap).addTailSink(wcPipe, outTap);

		Properties properties = new Properties();
		FlowConnector flowConnector = new HadoopFlowConnector(properties);
		flowConnector.connect(flowDef).complete();;
	}
}