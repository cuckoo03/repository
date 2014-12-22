package com;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.channels.SocketChannel;

public class Exam14_16SSConnectionTest {

	private static final int PORT = 10001;
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		InetSocketAddress isa = new InetSocketAddress("127.0.0.1", PORT);
		
		// 생성후 connect 함수 호출시에 주어진 어드레스로 접속
		SocketChannel sc = SocketChannel.open(isa);
		
		// SocketChannel sc = SocketChannel.open();
		// 소켓채널을 생성과 동시에 원격지로 연결
		
		sc.configureBlocking(false);
		
		System.out.println("is connected:" + sc.isConnected());
		System.out.println("is connection pending:" + sc.isConnectionPending());
		
//		sc.connect(isa);
		System.out.println("connect");
		System.out.println("is connected:" + sc.isConnected());
		System.out.println("is connection pending:" + sc.isConnectionPending());
		
		sc.finishConnect();
		System.out.println("finish connect");
		System.out.println("is connected:" + sc.isConnected());
		System.out.println("is connection pending:" + sc.isConnectionPending());
		
		System.out.println("is blocking mode:" + sc.isBlocking());
	}

}
