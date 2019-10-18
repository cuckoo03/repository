package com.elasticsearch.client.entity

import org.elasticsearch.search.aggregations.bucket.terms.Terms

class TermsResult {
	Terms terms
	long totalHits
}
