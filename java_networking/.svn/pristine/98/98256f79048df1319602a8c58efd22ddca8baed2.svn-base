package com;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Timer;
import java.util.TimerTask;

public class Exam14_18UDPEchoClient {
	private static Timer timer = null;

	public Exam14_18UDPEchoClient(int seconds) throws InterruptedException {
		timer = new Timer();
		timer.schedule(new EchoClientTask(), seconds * 1000);

		Thread.sleep(10000);
		timer.cancel();
	}

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		new Exam14_18UDPEchoClient(2);
	}
}

class EchoClientTask extends TimerTask {

	@Override
	public void run() {
		try {
			DatagramChannel channel = DatagramChannel.open();
			SocketAddress sa = new InetSocketAddress("localhost", 8080);
			ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

			while (!Thread.interrupted()) {
				buffer.clear();
				buffer.put("데이터그램채널 테스트".getBytes());
				buffer.flip();
				channel.send(buffer, sa);

				buffer.clear();
				SocketAddress addr = channel.receive(buffer);
				buffer.flip();

				byte[] bb = new byte[buffer.limit()];
				buffer.get(bb, 0, buffer.limit());
				String data = new String(bb);
				System.out.println("client receive:" + data);

				Thread.sleep(3000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}