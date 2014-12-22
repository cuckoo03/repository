package com;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class Exam14_6FileLockTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FileChannel channel = null;
		try {
			File file = new File("data.bin");
			channel = new RandomAccessFile(file, "rw").getChannel();
			
			FileLock lock = channel.lock(0, Long.MAX_VALUE, true);
			try {
				boolean isShared = lock.isShared();
				System.out.println("is shared:" + isShared);
			} finally {
				lock.release();
				channel.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}
}
