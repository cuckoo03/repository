package com;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Exam4_9 {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		FileOutputStream fos = null;
		DataOutputStream dos = null;
		
		try {
			fos = new FileOutputStream("data.bin");
			dos = new DataOutputStream(fos);
			dos.writeBoolean(true);
			dos.writeByte((byte)5);
			dos.writeInt(100);
			dos.writeDouble(200.5);
			dos.writeUTF("hello world");
			System.out.println("저장");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dos.close();
			fos.close();
		}
	}

}
