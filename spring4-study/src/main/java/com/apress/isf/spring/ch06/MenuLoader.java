package com.apress.isf.spring.ch06;

import java.io.InputStream;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
public class MenuLoader {
	@Autowired
	private ResourceLoader resource;

	public void print(String menu) {
		try {
			// getResouce의 인자가 classpath:로 시작한다
			InputStream stream = resource.getResource(menu).getInputStream();
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