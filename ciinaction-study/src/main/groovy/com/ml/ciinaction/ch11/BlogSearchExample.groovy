package com.ml.ciinaction.ch11

import groovy.transform.TypeChecked;

@TypeChecked
class BlogSearchExample {
//	public BlogQueryResult getBlogsFromTechnorati(String tag) {
//		return null
//	}
	static main(args) {
		def bs = new BlogSearchExample()
		def tag = "collective intelligence"
		def luceneIndexPath = "blogSearchIndex-compound"
		
//		def blogQueryResult = bs.getBlogsFromTechnorati(tag)
	}
}
