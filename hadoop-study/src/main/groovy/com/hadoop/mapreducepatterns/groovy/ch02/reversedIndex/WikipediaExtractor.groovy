package com.hadoop.mapreducepatterns.groovy.ch02.reversedIndex

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.hadoop.io.LongWritable
import org.apache.hadoop.io.Text
import org.apache.hadoop.mapreduce.Mapper
import org.apache.hadoop.mapreduce.Mapper.Context

import com.hadoop.mapreducepatterens.MRDPUtils
class WikipediaExtractor extends Mapper<LongWritable, Text, Text, Text>{
	private Text link = new Text()
	private Text outkey = new Text()
	public void map(LongWritable key, Text value, Context context) {
		Map<String, String> parsed = MRDPUtils.transformXmlToMap(value.toString())

		String txt = parsed.get("Body")
		String posttype = parsed.get("PostTypeId")
		String rowId = parsed.get("Id")

		if (txt == null || (posttype != null && posttype.equals("1"))) {
			return
		}

		txt = StringEscapeUtils.unescapeHtml(txt.toLowerCase())
		context.getCounter("Wiki", "Number of mapper").increment(1);
		link.set(getWikipediaURL(txt))
		outkey.set(rowId)
		context.write(link, outkey)
	}
	private String getWikipediaURL(String text) {
		int idx= text.indexOf("\"http://en.wikipedia.org")
		println idx == -1
		println idx.equals(-1)
		if (idx == -1) {
			return null
		}
		int idxEnd = text.indexOf('"', idx + 1)

		if (idxEnd == -1) {
			return null
		} 

		int idxHash = text.indexOf("#", idx + 1)

		if (idxHash != -1 && idxHash < idxEnd) {
			return text.substring(idx + 1, idxHash)
		} else {
			return text.substring(idx + 1, idxEnd)
		}
	}
}
