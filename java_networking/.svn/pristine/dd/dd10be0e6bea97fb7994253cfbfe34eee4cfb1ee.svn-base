package com;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Exam7_2 {

	/**
	 * @param args
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) throws UnknownHostException {
		// TODO Auto-generated method stub
		InetAddress inetaddr = null;
		inetaddr = InetAddress.getLocalHost();
		
		System.out.println(inetaddr.getHostName());
		System.out.println(inetaddr.getHostAddress());
		
		System.out.println("byte[] 형식의 주소 값읠 출력");
		byte[] ip = inetaddr.getAddress();
		for (byte item : ip) {
			System.out.println(item);
		}
	}

}
