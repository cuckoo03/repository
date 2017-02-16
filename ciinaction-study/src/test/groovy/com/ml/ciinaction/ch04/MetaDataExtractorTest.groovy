package com.ml.ciinaction.ch04

import groovy.transform.TypeChecked

import org.codehaus.groovy.runtime.DefaultGroovyMethods.TakeIterator;
import org.junit.Test

import com.ml.ciinaction.ch03.HTMLTagCloudDecorator
import com.ml.ciinaction.ch03.LinearFontSizeComputationStrategy
import com.ml.ciinaction.ch03.TagCloud
import com.ml.ciinaction.ch03.TagCloudImpl

@TypeChecked
class MetaDataExtractorTest {
	private static final String title = "Collective Intellignece and Web2.0"
	private static final String body = "Web2.0 is all about connecting users to users, " +
	"inviting users to participate and applying their collective " +
	"intelligence to improve the application. Collective intelligence " +
	"enhances the user experience"
	private static final int numSizes = 3
	private static final String fontPrefix = "font-size: "
	
	@Test
	def void testSimpleMetaDataExtractor() {
		def ex = new SimpleMetaDataExtractor()
		def fileName = "simpleExtractorChap3.html"
		generateTagCloud(ex, fileName)
	}
	
	private static void generateTagCloud(MetaDataExtractor ex, String fileName) {
		def mdv = ex.extractMetaData(title, body)
		def strategy = new LinearFontSizeComputationStrategy(numSizes, fontPrefix)
		// groovy overloading issue 
		def tagCloud = new TagCloudImpl(mdv as MetaDataVector, strategy)
		writeToFile(fileName, tagCloud)
	}
	
	private static void writeToFile(String fileName, TagCloud cloud) {
		def out = new BufferedWriter(new FileWriter(fileName))
		def decorator = new HTMLTagCloudDecorator()
		out.write(decorator.decorateTagCloud(cloud))
		out.close()
	}
}
