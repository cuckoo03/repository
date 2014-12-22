package com;

import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Exam9_3UTPTimeServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
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

				SimpleDateFormat format = new SimpleDateFormat(
						"yyyy-mm-dd HH:mm:ss a");
				String sdate = format.format(new Date());

				System.out.println(receivePacket.getAddress().getHostAddress()
						+ "에 현재시간 " + sdate + "을 전송합니다");

				DatagramPacket sendPacket = new DatagramPacket(
						sdate.getBytes(), sdate.getBytes().length,
						receivePacket.getAddress(), receivePacket.getPort());
				dsocket.send(sendPacket);
			}
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			if (dsocket != null) {
				dsocket.close();
			}
		}
	}

}
