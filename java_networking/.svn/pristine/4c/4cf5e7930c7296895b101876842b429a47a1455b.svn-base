package com;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Exam9_4UTPTimeClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String ip = "127.0.0.1";
		int port = 50001;
		InetAddress inetaddr = null;
		try {
			inetaddr = InetAddress.getByName(ip);
		} catch (Exception e) {
			System.out.println("잘못된 도메인이나 ip입니다");
			System.exit(1);
		}

		DatagramSocket dsocket = null;
		try {
			dsocket = new DatagramSocket();
			String line = null;

			DatagramPacket sendPacket = new DatagramPacket("".getBytes(), ""
					.getBytes().length, inetaddr, port);
			dsocket.send(sendPacket);
			
			byte[] buffer = new byte[1024];
			DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
			dsocket.receive(receivePacket);
			
			String msg = new String(receivePacket.getData());
			System.out.println("서버로부터 전달받은 시가:" + msg.trim());
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			if (dsocket != null) {
				dsocket.close();
			}
		}
	}

}
