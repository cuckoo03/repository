/*
 * Reciper Data Acquisition Server.
 *
 * Copyright (C) 2009  Lambert Park
 * Email: LambertPark@gmail.com
 */
package com.netty.log;

import org.jboss.netty.channel.ChannelHandler;

/**
 * ThroughputMonitor.java - 매 3초 단위로 현재의 처리량을 측정하여 표시한다.
 * 
 * @author lambert
 * @version 1.0
 */
public class ThroughputMonitor extends Thread {

	private final ChannelHandler handler;

	public ThroughputMonitor(ChannelHandler handler) {
		this.handler = handler;
	}

	@Override
	public void run() {
		long oldCounter = getTransferredBytes();
		long startTime = System.currentTimeMillis();
		for (;;) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			long endTime = System.currentTimeMillis();
			long newCounter = getTransferredBytes();
			System.err.format("%4.3f MiB/s%n", (newCounter - oldCounter)
					* 1000.0 / (endTime - startTime) / 1048576.0);
			oldCounter = newCounter;
			startTime = endTime;
		}
	}

	private long getTransferredBytes() {
			return ((LogCrawlerServerHandler) handler).getReceiveData();
	}
}
