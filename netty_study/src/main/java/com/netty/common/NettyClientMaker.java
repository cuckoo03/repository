package com.netty.common;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NettyClientMaker {
	public static void main(String[] args) throws InterruptedException {
		String host = "192.168.1.100";
//		String host = "127.0.0.1";
		int port = 9001;
		ExecutorService executor = Executors.newCachedThreadPool();
		for (int i = 0; i < 1; i++) {
			executor.execute(new NettyClientThread(host, port, String
					.valueOf(i)));
			Thread.sleep(100);
		}
	}
}

class NettyClientThread implements Runnable {
	private final NettyClient nettyClient;
	private DataChangeEventListener listener;
	private final String msg;

	public NettyClientThread(String host, int port, String msg) {
		this.msg = msg;
		nettyClient = new NettyClient(host, port);
		listener = new MyListener();
	}

	@Override
	public void run() {
		nettyClient.start();
		nettyClient.send(msg);
	}
}

class MyListener implements DataChangeEventListener {
	@Override
	public void dataChanged(Object data) {
		System.out.println("listener dataChanged:" + data);
	}
}