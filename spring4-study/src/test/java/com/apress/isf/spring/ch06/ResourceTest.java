package com.apress.isf.spring.ch06;

import java.io.InputStream;
import java.util.Scanner;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;

public class ResourceTest {
	@Test
	public void test() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"spring/application-context.xml");
		System.out.println(context);

		Resource resource = context.getResource("data/menu.txt");
		try {
			InputStream stream = resource.getInputStream();
			Scanner scanner = new Scanner(stream);
			while (scanner.hasNext()) {
				System.out.println(scanner.nextLine());
			}
			scanner.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
