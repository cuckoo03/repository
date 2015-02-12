package com.cascading.definitive.ch01;

import java.util.Properties;

import cascading.flow.FlowConnector;
import cascading.flow.FlowDef;
import cascading.flow.hadoop.HadoopFlowConnector;
import cascading.pipe.Pipe;
import cascading.scheme.hadoop.TextDelimited;
import cascading.tap.Tap;
import cascading.tap.hadoop.Hfs;

public class Example01 {
	public static void main(String[] args) {
		String inPath = args[0];
		String outPath = args[1];

		// create the source tap
		Tap inTap = new Hfs(new TextDelimited(true, "\t"), inPath);

		// create the sink tap
		Tap outTap = new Hfs(new TextDelimited(true, "\t"), outPath);

		// specify a pipe to connect the taps
		Pipe copyPipe = new Pipe("copy");

		// connect the taps, pipes, etc., into a flow
		FlowDef flowDef = FlowDef.flowDef().addSource(copyPipe, inTap)
				.addTailSink(copyPipe, outTap);

		// run the flow
		Properties properties = new Properties();
//	    AppProps.setApplicationJarClass( properties, Main.class );
		FlowConnector flowConnector = new HadoopFlowConnector(properties);
		flowConnector.connect(flowDef).complete();
	}
}