package com;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Exam11_1MulticastServer extends Thread {
	DatagramSocket socket = null;
	DatagramPacket packet = null;
	InetAddress channel = null;
	int port = 200001;
	String address = "239.0.0.1";
	boolean onAir = true;
	
	public Exam11_1MulticastServer() throws IOException {
		super("멀티캐스트 방송국");
		socket = new DatagramSocket(port);
	}
	public void run() {
		String msg = "멀티캐스트 방송";
		byte[] b = new byte[100];
		while (onAir) {
			try {
				b = msg.getBytes();
				channel = InetAddress.getByName(address);
				packet = new DatagramPacket(b, b.length, channel, port);
				socket.send(packet);
				try {
					sleep(1000);
					System.out.println("방송중");
				} catch(InterruptedException e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				
			}
		}
		socket.close();
	}
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		new Exam11_1MulticastServer().start();
	}

}
