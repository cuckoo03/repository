package com.hadoop.mapreducepatterns.groovy.ch03.counter

import org.apache.hadoop.io.LongWritable
import org.apache.hadoop.io.NullWritable
import org.apache.hadoop.io.Text
import org.apache.hadoop.mapreduce.Mapper
import org.apache.hadoop.mapreduce.Mapper.Context

import com.hadoop.mapreducepatterns.MRDPUtils

class CountNumUsersByStateMapper extends Mapper<LongWritable, Text,
NullWritable, NullWritable> {
	public static final String STATE_COUNTER_GROUP = "State"
	private String[] statesArray = [
		"AL",
		"AK",
		"AZ",
		"AR",
		"CA",
		"CO",
		"CT",
		"DE",
		"FL",
		"GA",
		"HI",
		"ID",
		"IL",
		"IN",
		"IA",
		"KS",
		"KY",
		"LA",
		"ME",
		"MD",
		"MA",
		"MI",
		"MN",
		"MS",
		"MO",
		"MT",
		"NE",
		"NV",
		"NH",
		"NJ",
		"NM",
		"NY",
		"NC",
		"ND",
		"OH",
		"OK",
		"OR",
		"PA",
		"RI",
		"SC",
		"SF",
		"TN",
		"TX",
		"UT",
		"VT",
		"VA",
		"WA",
		"WV",
		"WI",
		"WY"
	]
	private HashSet<String> states = new HashSet<String>(Arrays
	.asList(statesArray))

	@Override
	public void map(LongWritable key, Text value, Context context) {
		Map<String, String> parsed = MRDPUtils.transformXmlToMap(value.toString())

		String location = parsed.get("Location")
		if (location != null && !location.isEmpty()) {
			boolean unknown = true
			String[] tokens = location.toUpperCase().split("\\s")
			for (String state : tokens) {
				if (states.contains(state)) {
					context.getCounter(STATE_COUNTER_GROUP, state).increment(1)
					unknown = false
					break
				}
			}

			if (unknown) {
				context.getCounter(STATE_COUNTER_GROUP, "Unknown").increment(1)
			} else {
				context.getCounter(STATE_COUNTER_GROUP, "NullOrEmpty")
						.increment(1)
			}
		}
	}
}