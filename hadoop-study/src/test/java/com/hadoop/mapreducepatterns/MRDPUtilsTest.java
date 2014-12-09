package com.hadoop.mapreducepatterns;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import com.hadoop.mapreducepatterens.MRDPUtils;

public class MRDPUtilsTest {
	private String xml;

	@BeforeClass
	public static void before() throws IOException {
		InputStream in = MRDPUtilsTest.class.getClassLoader()
				.getResourceAsStream("ubuntu/data/comments.xml");
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String line = null;
		while ((line = br.readLine()) != null) {
			Map<String, String> map = MRDPUtils.transformXmlToMap(line);
			if (!map.containsKey("UserId")) {
				System.out.println("");
				System.out.println(map);
			}
		}
	}

	@Test
	public void transformXmlToMapTest() {

	}
}
