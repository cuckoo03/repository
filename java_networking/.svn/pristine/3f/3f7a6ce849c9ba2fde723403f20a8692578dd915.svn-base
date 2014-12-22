package com;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class Exam14_14CopyTransferFromTest extends MyTimer {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		start();
		copyTransferFrom();
		end("copyTransferFrom");
	}

	private static void copyTransferFrom() throws IOException {
		// TODO Auto-generated method stub
		FileInputStream fis = null;
		FileOutputStream fos = null;
		FileChannel in = null;
		FileChannel out = null;
		try {
			fis = new FileInputStream(filePath);
			fos = new FileOutputStream("srccopy.zip");
			in = fis.getChannel();
			out = fos.getChannel();

			out.transferFrom(in, 0, in.size());
		} finally {
			in.close();
			out.close();
		}
	}

}
