package com.storm.starter

class TransactionalWords {
	static main(args) {
		Integer prevCount = null
		int count = 0
		BigInteger txid = null
	}

	public static class BucketValue {
		int count = 0
		BigInteger txid
	}
	
	public static final int BUCKET_SIZE = 10
	
}
