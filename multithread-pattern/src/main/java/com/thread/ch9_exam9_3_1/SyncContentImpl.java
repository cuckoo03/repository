package com.thread.ch9_exam9_3_1;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class SyncContentImpl implements Content {
	private byte[] contentbytes;

	public SyncContentImpl(String urlstr) {
		System.out.println(Thread.currentThread().getName() + ":Getting "
				+ urlstr);
		DataInputStream in = null;
		try {
			URL url = new URL(urlstr);
			in = new DataInputStream(url.openStream());
			byte[] buffer = new byte[1];
			int index = 0;
			try {
				while (true) {
					int c = in.readUnsignedByte();
					if (buffer.length <= index) {
						byte[] largerbuffer = new byte[buffer.length * 2];
						System.arraycopy(buffer, 0, largerbuffer, 0, index);
						buffer = largerbuffer;
					}
					buffer[index++] = (byte) c;
				}
			} catch (EOFException e) {
//				e.printStackTrace();
			} finally {
				in.close();
			}
			contentbytes = new byte[index];
			System.arraycopy(buffer, 0, contentbytes, 0, index);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public byte[] getBytes() {
		return contentbytes;
	}
}
