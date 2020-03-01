package com.elasticsearch.client.entity

import org.elasticsearch.search.aggregations.bucket.terms.Terms

class TermsResult {
	Terms terms
	long totalHits
	public Terms getTerms() {
		return terms;
	}
	public void setTerms(Terms terms) {
		this.terms = terms;
	}
	public long getTotalHits() {
		return totalHits;
	}
	public void setTotalHits(long totalHits) {
		this.totalHits = totalHits;
	}
}
