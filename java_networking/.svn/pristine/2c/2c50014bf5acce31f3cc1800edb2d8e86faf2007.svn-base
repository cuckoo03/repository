package com;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

public class Exam14_5SharedFileChannelInstanceTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		RandomAccessFile raf;
		try {
			raf = new RandomAccessFile("data.bin", "rw");
			
			raf.seek(1000);
			FileChannel fc = raf.getChannel();
			System.out.println("File position:" + fc.position());
			
			raf.seek(500);
			System.out.println("File position:" + fc.position());
			
			fc.position(100);
			System.out.println("file position:" + raf.getFilePointer());
			
			System.out.println("size:" + raf.length());
			System.out.println("channel size:" + fc.size());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}