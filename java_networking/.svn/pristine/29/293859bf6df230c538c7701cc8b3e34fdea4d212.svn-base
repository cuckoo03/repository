package com.rmi;

import java.rmi.Naming;

public class RMIHelloClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Object obj = Naming.lookup("//127.0.0.1:1099/hello");
			RMIHello hi = (RMIHello) obj;
			String msg = hi.hello("hi");
			System.out.println(msg);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
