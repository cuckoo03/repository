package com.luceneinaction.ch04

import java.io.IOException;

import groovy.transform.TypeChecked;

@TypeChecked
class MockSynonymEngine implements SynonymEngine {
	private static Map map = new HashMap()
	static {
		map.put("quick", ["fast", "speedy"])
		map.put("jumps", ["leaps", "hops"])
		map.put("over", ["above"])
		map.put("lazy", ["apathetic", "sluggish"])
		map.put("dogs", ["canines", "pooches"])
	}

	@Override
	public String[] getSynonyms(String termText) throws IOException {
		return map.get(termText) as String[]
	}
}
