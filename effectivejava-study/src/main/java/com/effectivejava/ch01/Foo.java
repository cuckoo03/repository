package com.effectivejava.ch01;

import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public abstract class Foo {
	private static Map<String, Class<?>> implementations = null;
	private static ResourceBundle classNames;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static synchronized void initMapIfnecessary() {
		if (implementations == null)
			implementations = new HashMap();

		String firstFoo = getValue("firstFoo");
		String secondFoo = getValue("firstFoo");
		try {
			Class obj1 = Class.forName(firstFoo);
			Class obj2 = Class.forName(secondFoo);
			implementations.put("firstFoo", obj1);
			implementations.put("secondFoo", obj2);
		} catch (ClassNotFoundException e) {
			System.out.println("class not found");
		}
	}

	private static String getValue(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	static {
		try {
			classNames = ResourceBundle.getBundle("TableText");
		} catch (MissingResourceException e) {
			System.out.println("Resource file not found");
			System.exit(1);
		}
	}

	@SuppressWarnings("rawtypes")
	public static Foo getInstance(String key) {
		initMapIfnecessary();
		Class c = implementations.get(key);
		System.out.println(c + " " + "returned");
		if (c == null)
			return new DefaultFoo();
		try {
			return (Foo) c.newInstance();
		} catch (Exception e) {
			return new DefaultFoo();
		}
	}
	
	public static void main(String[] args) {
		
	}
}
