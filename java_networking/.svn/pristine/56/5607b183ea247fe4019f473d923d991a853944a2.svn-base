package com.utils;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class SystemVar {
	public static final String LINE_SEPARATOR = System
			.getProperty("line.separator");

	public static final String FILE_SEPARATOR = System
			.getProperty("file.separator");

	/**
	 * not crossplatform safe
	 */
	public static final String FILE_ENCODING = System
			.getProperty("file.encoding");

	public static final String OS_ENCODING = System
			.getProperty("sun.jun.encoding");

	public static final String IO_UNICODE_ENCODING = System
			.getProperty("sun.io.unicode.encoding");

	public static final String SYSTEM_IN_ENCODING = new InputStreamReader(
			System.in).getEncoding();

	/**
	 * JDK 1.4
	 */
	public static final String BYTE_ARRAY_ENCODING = new OutputStreamWriter(
			new java.io.ByteArrayOutputStream()).getEncoding();
	
	/**
	 * JDK 1.5
	 */
	public static final String DEFAULT_CHARSET = Charset.defaultCharset().name();
	
	public static final void printSystemProperties() {
		System.getProperties().list(System.out);
	}

	public static final void init() {
		try {
			System.setOut(new PrintStream(System.out, true,
					SystemVar.OS_ENCODING));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final void printEncodingTypes() {
		System.out.println("OS_ENCODING=" + OS_ENCODING);
		System.out.println("FILE_ENCODING=" + FILE_ENCODING);
		System.out.println("IO_UNICODE_ENCODING=" + IO_UNICODE_ENCODING);
		System.out.println("SYSTEM_IN_ENCODING=" + SYSTEM_IN_ENCODING);
		System.out.println("BYTE_ARRAY_ENCODING=" + BYTE_ARRAY_ENCODING);
		System.out.println("DEFAULT CHARSET=" + DEFAULT_CHARSET);
	}
	
	public static final void printAvailableCharset() {
		Map map = Charset.availableCharsets();
		Set set = map.entrySet();
		Iterator it = set.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String name = (String) entry.getKey();
			Charset chs = (Charset) entry.getValue();
			System.out.println(name);
			Set aliases = chs.aliases();
			for (Iterator it2 = aliases.iterator(); it2.hasNext();) {
				System.out.println("\t" + it2.next());
			}
		}		
	}
	
}
