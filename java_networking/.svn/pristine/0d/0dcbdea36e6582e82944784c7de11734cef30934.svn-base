package com;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Exam9_1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int port = 50001;
		DatagramSocket dsocket = null;
		try {
			System.out.println("접속 대기상태입니다");
			dsocket = new DatagramSocket(port);
			String line = null;
			while (true) {
				byte[] buffer = new byte[1024];
				DatagramPacket receivePacket = new DatagramPacket(buffer,
						buffer.length);
				dsocket.receive(receivePacket);

				String msg = new String(receivePacket.getData(), 0,
						receivePacket.getLength());
				System.out.println("전송 받은 문자열:" + msg);
				if (msg.equals("quit")) {
					break;
				}

				DatagramPacket sendPacket = new DatagramPacket(receivePacket
						.getData(), receivePacket.getData().length,
						receivePacket.getAddress(), receivePacket.getPort());
				dsocket.send(sendPacket);
			}
			System.out.println("UDP EchoServer를 종료합니다");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dsocket != null) {
				dsocket.close();
			}
		}
	}
}
