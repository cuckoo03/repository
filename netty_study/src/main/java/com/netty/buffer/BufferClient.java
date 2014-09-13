package com.netty.buffer;

import com.netty.common.NettyClient;

public class BufferClient {

	public static void main(String[] args) {
		NettyClient client = new NettyClient("127.0.0.1", 10001);
		client.start();
		client.addEventListener(new DataObserver());
		while (true) {
			try {
				client.send();
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
