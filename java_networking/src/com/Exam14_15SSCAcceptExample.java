package com;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Exam14_15SSCAcceptExample {

	/**
	 * @param args
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		ServerSocketChannel ssc = ServerSocketChannel.open();
		ssc.socket().bind(new InetSocketAddress(8080));
		ssc.configureBlocking(false);
		while (true) {
			System.out.println("wait for connecting");

			try {
				SocketChannel sc = ssc.accept();
				if (null == sc) {
					Thread.sleep(1000);
				} else {
					System.out.println(sc.socket().getRemoteSocketAddress()
							+ "가 연결을 시도했습니다");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
