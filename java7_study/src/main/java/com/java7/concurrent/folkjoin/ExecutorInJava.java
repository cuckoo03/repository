package com.java7.concurrent.folkjoin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecutorInJava {

	public static void main(String[] args) {
		List<Student> students = new ArrayList<Student>();

		students.add(new Student("Bob", 66, 80, 95));
		students.add(new Student("Tom", 94, 82, 72));
		students.add(new Student("Joy", 88, 85, 99));
		students.add(new Student("Mills", 82, 75, 89));

		ExecutorService executor = Executors.newFixedThreadPool(3);

		List<Future<Float>> results = null;

		try {
			results = executor.invokeAll(students);
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (Future<Float> future : results) {
			try {
				System.out.println(future.get());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
	}
}

class Student implements Callable<Float> {
	private String name;
	private Integer subject1;
	private Integer subject2;
	private Integer subject3;

	public Student(String name, Integer subject1, Integer subject2,
			Integer subject3) {
		super();
		this.name = name;
		this.subject1 = subject1;
		this.subject2 = subject2;
		this.subject3 = subject3;
	}

	@Override
	public Float call() throws Exception {
		return (subject1 + subject2 + subject3) / 3.0f;
	}
}
