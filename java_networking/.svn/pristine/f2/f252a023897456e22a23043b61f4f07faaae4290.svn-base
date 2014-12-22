package com.ch16.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.ch16.queue.Queue;

public class DynamicClassLoader {

	@SuppressWarnings("unchecked")
	public static Object createInstance(String type, Queue queue)
			throws SecurityException, NoSuchMethodException,
			ClassNotFoundException, IllegalArgumentException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {
		Object obj = null;
		if (null != type) {
			Class[] paramType = new Class[] { Queue.class };
			Constructor con = Class.forName(type).getConstructor(paramType);
			Object[] params = new Object[] { queue };
			obj = con.newInstance(params);
		}
		return obj;
	}
}
