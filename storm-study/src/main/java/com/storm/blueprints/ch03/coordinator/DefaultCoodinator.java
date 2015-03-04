package com.storm.blueprints.ch03.coordinator;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import storm.trident.spout.ITridentSpout.BatchCoordinator;

public class DefaultCoodinator implements BatchCoordinator<Long>, Serializable {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory
			.getLogger(DefaultCoodinator.class);

	@Override
	public Long initializeTransaction(long txid, Long prevMetadata,
			Long currMetadata) {
		LOG.info("Initializing Transcation [{}]", txid);
		return null;
	}

	@Override
	public void success(long txid) {
		LOG.info("Successful Transcation [{}]", txid);
	}

	@Override
	public boolean isReady(long txid) {
		return true;
	}

	@Override
	public void close() {
	}
}