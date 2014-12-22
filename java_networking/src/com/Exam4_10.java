package com;

import java.io.DataInputStream;
import java.io.FileInputStream;

public class Exam4_10 {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FileInputStream fis = null;
		DataInputStream dis = null;
		try {
			fis = new FileInputStream("data.bin");
			dis = new DataInputStream(fis);
			boolean b = dis.readBoolean();
			byte b2 = dis.readByte();
			int i = dis.readInt();
			double d = dis.readDouble();
			String s = dis.readUTF();
			System.out.println("boolean:"+b);
			System.out.println("byte:"+b2);
			System.out.println("int:"+i);
			System.out.println("double:"+d);
			System.out.println("string:"+s);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
		}
	}

}
