package com.nio;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import com.utils.SystemVar;

public class CharsetTest {

	/**
	 * bytebuffer 의 charset 예제
	 * 
	 * @param args
	 * @throws UnsupportedEncodingException
	 */
	public static void main(String[] args) throws UnsupportedEncodingException {

		FileChannel fileChannel = null;

		try {
			String systemCharset = new InputStreamReader(System.in)
					.getEncoding();
			SystemVar.printEncodingTypes();
			
			Charset charset = Charset.forName(systemCharset);
			System.out.println("charset:"+charset);
			ByteBuffer buff = charset.encode("한글과 Alphabet");

			FileOutputStream fos = new FileOutputStream("CharsetTest.tmp");
			fileChannel = fos.getChannel();
			fileChannel.write(buff);
			
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fileChannel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
}
