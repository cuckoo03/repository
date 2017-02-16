package com.ml.ciinaction.ch03

import groovy.transform.TypeChecked

@TypeChecked
class HTMLTagCloudDecorator implements VisualizeTagCloudDecorator {
	private static final String HEADER_HTML = "<html><br><head><br><title>TagCloud<br></title><br></head>"
	private static final int NUM_TAGS_IN_LINE = 10
	private Map<String, String> fontMap

	public HTMLTagCloudDecorator() {
		getFontMap()
	}

	// html 파일을 생성
	@Override
	public String decorateTagCloud(TagCloud tagCloud) {
		def sw = new StringWriter()
		def elements = tagCloud.getTagCloudElements()
		sw.append(HEADER_HTML)
		sw.append("<br><body><h3>TagCloud(" + elements.size() + ")</h3>")

		def count = 0
		for (TagCloudElement tce : elements) {
			sw.append("&nbsp;<a style=\"" + this.fontMap.get(tce.getFontSize()) +";\">")
			sw.append(tce.getTagText() + "</a>&nbsp;")
			if (count++ == NUM_TAGS_IN_LINE) {
				count = 0
				sw.append("<br>")
			}
		}

		sw.append("<br></body><br></html>")

		return sw.toString()
	}

	// xml 파일이나 폰트 빈과 매핑
	private void getFontMap() {
		this.fontMap = new HashMap<>()
		fontMap.put("font-size: 0", "font-size: 13px")
		fontMap.put("font-size: 1", "font-size: 20px")
		fontMap.put("font-size: 2", "font-size: 24px")
	}
}
