package com.nio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.utils.SystemVar;

public class CharsetTest2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FileChannel channel = null;

		
//		Charset charset = Charset.forName(new InputStreamReader(System.in)
//				.getEncoding());
		Charset charset = Charset.forName("UTF-8");
		SystemVar.printEncodingTypes();
		System.out.println("charset:" + charset);
		ByteBuffer buffer = ByteBuffer.allocate(32);

		try {
			FileInputStream fis = new FileInputStream("CharsetTest.tmp");
			channel = fis.getChannel();
			channel.read(buffer);
			buffer.flip();

			CharBuffer charBuffer = charset.decode(buffer);
			System.out.println(charBuffer.toString());
			
//			SystemVar.printAvailableCharset();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}