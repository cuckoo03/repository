package com;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class Exam14_13CopyTransferToTest extends MyTimer {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		start();
		copyTransferTo();
		end("copyTransferTo");
	}

	private static void copyTransferTo() throws IOException {
		// TODO Auto-generated method stub
		FileInputStream fis = null;
		FileOutputStream fos = null;
		FileChannel in = null;
		FileChannel out = null;
		try {
			fis = new FileInputStream(filePath);
			fos = new FileOutputStream("mp3.mp3");
			in = fis.getChannel();
			out = fos.getChannel();

			in.transferTo(0, in.size(), out);
		} finally {
			in.close();
			out.close();
		}
	}

}
