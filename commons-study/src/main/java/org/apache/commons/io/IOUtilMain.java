package org.apache.commons.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class IOUtilMain {

	public static void main(String[] args) throws IOException {
		InputStream in = null;
		try {
			in = new URL("http://jakarta.apache.org").openStream();
			/*
			InputStreamReader isr = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(isr);
			String line;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
			*/
			System.out.println(IOUtils.toString(in));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(in);
			in.close();
		}
	}
}
