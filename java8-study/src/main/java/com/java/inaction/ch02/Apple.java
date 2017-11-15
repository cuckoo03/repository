package com.java.inaction.ch02;

public class Apple implements Comparable<Apple> {
	private Integer weight;

	public Apple() {};
	
	public Apple(int weight) {
		this.weight = weight;
	}

	public Apple(String color, Integer weight) {
		
	}
	
	public int getWeight() {
		return weight;
	}

	@Override
	public int compareTo(Apple o) {
		return 0;
	}

}
