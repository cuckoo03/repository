package com.ml.ciinaction.ch03

import groovy.transform.TypeChecked

import org.junit.Test

@TypeChecked
class TagCloudTest {
	@Test
	public void testTagCloud() throws Exception {
		def firstString = "binary"
		def numSizes = 3
		def fontPrefix = "font-size: "
		
		def l = new ArrayList<TagCloudElement>()
		l.add(new TagCloudElementImpl("tagging", 1))
		l.add(new TagCloudElementImpl("schema", 3))
		l.add(new TagCloudElementImpl("denormalized", 1))
		l.add(new TagCloudElementImpl("database", 2))
		l.add(new TagCloudElementImpl("fristString", 1))
		l.add(new TagCloudElementImpl("normalized", 1))
		
		def strategy = new LinearFontSizeComputationStrategy(numSizes, fontPrefix)
		def cloudLinear = new TagCloudImpl(l, strategy)
		println cloudLinear
		
		strategy = new LogFontSizeComputationStrategy(numSizes, fontPrefix)
		def cloudLog = new TagCloudImpl(l, strategy)
		println cloudLog
		
		def fileName = "testTagCloudChap2.html"
		writeToFile(fileName,  cloudLinear)
		
	}
	
	private static void writeToFile(String fileName, TagCloud cloud) {
		def out = new BufferedWriter(new FileWriter(fileName))
		def decorator = new HTMLTagCloudDecorator()
		out.write(decorator.decorateTagCloud(cloud))
		out.close()
	}
}
