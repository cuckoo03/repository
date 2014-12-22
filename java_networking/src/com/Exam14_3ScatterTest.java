package com;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ScatteringByteChannel;

public class Exam14_3ScatterTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			FileInputStream fin = new FileInputStream("Exam10_3.html");
			ScatteringByteChannel channel = fin.getChannel();

			ByteBuffer header = ByteBuffer.allocateDirect(100);
			ByteBuffer body = ByteBuffer.allocateDirect(300);
			ByteBuffer[] buffers = { header, body };
			
			int readcount = (int) channel.read(buffers);
			channel.close();
			System.out.println("readcount:" + readcount);
			
			System.out.println("/////////////////");
			
			header.flip();
			body.flip();
			
			byte[] b = new byte[100];
			header.get(b);
			System.out.println("header:" + new String(b));
			
			System.out.println("/////////////////////////");
			
			byte[] bb = new byte[300];
			body.get(bb);
			System.out.println("body:" + new String(bb));
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
