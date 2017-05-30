package com.java.inaction.ch03;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Chapter3Main {

	public static void process(Runnable r) {
		r.run();
	}

	public static String processFile(BufferedReaderProcessor p)
			throws IOException {
		try (BufferedReader br = new BufferedReader(new FileReader("pom.xml"))) {
			return p.process(br);
		}
	}

	public static <T> void forEach(List<T> list, Consumer<T> c) {
		for (T i : list)
			c.accept(i);
	}
	
	public static <T, R> List<R> map(List<T> list, Function<T, R> f) {
		List<R> result = new ArrayList<>();
		for (T i : list) 
			result.add(f.apply(i));

		return result;
	}

	public static void main(String[] args) {
		process(() -> System.out.println("run"));
		process(() -> {
			System.out.println("run");
		});

		// recurrent pattern
		try {
			String line = processFile((BufferedReader br) -> br.readLine());
			System.out.println(line);
			String twoline = processFile(br -> {
				return br.readLine() + br.readLine();
			});
			System.out.println(twoline);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// consumer interface 
		forEach(Arrays.asList(1, 2, 3), i -> System.out.println("consumer:" + i));
		
		// function interface
		List<Integer> result = map(Arrays.asList(1, 2, 3), i-> i * i);
		for (Integer i : result) {
			System.out.println("function:" + i);
		}
		
		
		
	}
}
