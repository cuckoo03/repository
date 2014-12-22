package com;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.GatheringByteChannel;

public class Exam14_4GatheringTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			FileOutputStream fos = new FileOutputStream("Exam10_4.html");
			GatheringByteChannel channel = fos.getChannel();

			ByteBuffer header = ByteBuffer.allocateDirect(20);
			ByteBuffer body = ByteBuffer.allocateDirect(40);
			ByteBuffer[] buffers = { header, body };

			header.put("Hello".getBytes());
			body.put("world".getBytes());

			header.flip();
			body.flip();

			channel.write(buffers);
			channel.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
