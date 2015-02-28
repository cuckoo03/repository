package com.storm.examples.trident;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import storm.trident.spout.ITridentSpout.BatchCoordinator;

public class LogBatchCoordinator implements BatchCoordinator<Long> {
	private static final Logger LOG = LoggerFactory
			.getLogger(LogBatchCoordinator.class);

	@Override
	public Long initializeTransaction(long txid, Long prevMetadata,
			Long currMetadata) {
		if (prevMetadata == null) {
			return 1L;
		}
		return prevMetadata + 1L;
	}

	@Override
	public void success(long txid) {
		LOG.info("BatchCoordinator.success(txd={})", txid);
	}

	@Override
	public boolean isReady(long txid) {
		LOG.info("BatchCoordinator.isReady({})", new Object[] { txid });
		return true;
	}

	@Override
	public void close() {
		LOG.info("BatchCoordinator.close()");
	}
}