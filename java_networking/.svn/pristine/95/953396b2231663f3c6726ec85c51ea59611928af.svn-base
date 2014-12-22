package com;

import java.net.InetAddress;

public class Exam7_1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		InetAddress inetaddr[] = null;
		try {
			inetaddr = InetAddress.getAllByName("www.yahoo.com");
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (InetAddress item : inetaddr) {
			System.out.println(item.getHostName());
			System.out.println(item.getHostAddress());
			System.out.println(item.toString());
		}
	}

}
