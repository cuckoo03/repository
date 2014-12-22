package com;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class Exam14_10CopyMapTest extends MyTimer {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		start();
		copyMap();
		end("copyMap");
	}

	private static void copyMap() throws IOException {
		FileInputStream fis = null;
		FileOutputStream fos = null;
		FileChannel in = null;
		FileChannel out = null;
		try {
			fis = new FileInputStream(filePath);
			fos = new FileOutputStream("mp3.mp3");
			in = fis.getChannel();
			out = fos.getChannel();
			
			MappedByteBuffer m = in.map(FileChannel.MapMode.READ_ONLY, 0, in.size());
			out.write(m);
		} finally {
			if (null != in) {
				in.close();
			}
			if (null != out) {
				out.close();
			}
		}
	}

}
