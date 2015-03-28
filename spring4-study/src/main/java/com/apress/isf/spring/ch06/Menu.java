package com.apress.isf.spring.ch06;

import java.io.InputStream;
import java.util.Scanner;

import org.springframework.core.io.Resource;

public class Menu {
	private Resource resource;

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public void print() {
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
