package com.rmi;

import java.rmi.Naming;
import java.rmi.RMISecurityManager;

public class RMIHelloBind {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RMISecurityManager sm = new RMISecurityManager();
		System.setSecurityManager(sm);
		try {
			RMIHelloImpl obj = new RMIHelloImpl();
			Naming.rebind("127.0.0.1:1099/hello", obj);
			System.out.println("regist hello");
		} catch (Exception e) {
			System.out.println("regist error");
		}
	}

}
