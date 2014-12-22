package com;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import com.utils.SystemVar;

public class Exam9_2_client {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String ip = "127.0.0.1";
		int port = 0;
		try {
			port = Integer.parseInt("50001");
			
			SystemVar.printEncodingTypes();
			
		} catch (Exception e) {
			System.out.println(e);
			System.exit(1);
		}

		InetAddress inetaddr = null;
		try {
			inetaddr = InetAddress.getByName(ip);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("잘못된 도메인이나 ip입니다");
			System.exit(1);
		}

		DatagramSocket dsocket = null;
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in, SystemVar.FILE_ENCODING));
			dsocket = new DatagramSocket();
			String line = null;
			System.out.println("입력하세요");
			while ((line = br.readLine()) != null) {
				DatagramPacket sendPacket = new DatagramPacket(line.getBytes(),
						line.getBytes().length, inetaddr, port);
				dsocket.send(sendPacket);

				if (line.equals("quit")) {
					break;
				}

				byte[] buffer = new byte[line.getBytes().length];
				DatagramPacket receivePacket = new DatagramPacket(buffer,
						buffer.length);
				dsocket.receive(receivePacket);
				
//				String msg = new String(receivePacket.getData(), receivePacket.getData().length);
				String msg = new String(receivePacket.getData());
				System.out.println("전송받은 문자열:" + msg);
			}
			System.out.println("UDPEcho Client를종료합니다");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dsocket != null) {
				dsocket.close();
			} 
		}
	}
}
