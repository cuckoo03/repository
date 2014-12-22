package com;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class Exam4_11CopyByteBufferTest extends MyTimer {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		start();
		copyByteBuffer();
		end("copyByteBuffer");
	}

	private static void copyByteBuffer() throws IOException {
		// TODO Auto-generated method stub
		FileInputStream fis = null;
		FileOutputStream fos = null;
		FileChannel in = null;
		FileChannel out = null;
		try {
			fis = new FileInputStream(filePath);
			fos = new FileOutputStream("copyByteBuffer.zip");
			in = fis.getChannel();
			out = fos.getChannel();

			ByteBuffer buf = ByteBuffer.allocate((int) in.size());
			in.read(buf);
			buf.flip();
			out.write(buf);
		} finally {
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
		}
	}
}
