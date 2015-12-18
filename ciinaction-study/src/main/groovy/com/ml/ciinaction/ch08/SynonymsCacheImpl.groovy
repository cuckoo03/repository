package com.ml.ciinaction.ch08

import groovy.transform.TypeChecked

@TypeChecked
class SynonymsCacheImpl extends CacheImpl implements SynonymsCache {
	private Map<String, List<String>> synonyms

	public SynonymsCacheImpl() throws IOException {
		this.synonyms = new HashMap<>()
		def ciList = new ArrayList<String>()
		ciList.add("ci")
		this.synonyms.put(getStemmedText("collective intelligence"), ciList)
	}

	@Override
	public List<String> getSynonym(String text) throws IOException {
		return this.synonyms.get(getStemmedText(text))
	}
}
