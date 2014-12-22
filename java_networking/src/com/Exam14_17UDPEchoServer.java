package com;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class Exam14_17UDPEchoServer {
	protected int port;

	public Exam14_17UDPEchoServer(int port) {
		this.port = port;
	}

	public void execute() throws IOException, InterruptedException {
		DatagramChannel channel = DatagramChannel.open();
		channel.socket().bind(new InetSocketAddress("localhost", port));
		channel.configureBlocking(false);

		ByteBuffer buffer = ByteBuffer.allocate(1024);
		while (true) {
			buffer.clear();
			SocketAddress addr = channel.receive(buffer);

			if (null != addr) {
				System.out.println("패킷이 도착했습니다");
				
				buffer.flip();
				byte[] bb = new byte[buffer.limit()];
				buffer.get(bb, 0, buffer.limit());
				String data = new String(bb);
				System.out.println("server receive:" + data);
				
				buffer.flip();
				channel.send(buffer, addr);
			} else {
				System.out.println("도착한 패킷이 없습니다");
				Thread.sleep(1000);
			}
		}
	}

	/**
	 * @param args
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException,
			InterruptedException {
		// TODO Auto-generated method stub
		Exam14_17UDPEchoServer server = new Exam14_17UDPEchoServer(8080);
		server.execute();
	}

}
