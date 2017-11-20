package com.java.inaction.ch02;

public class Apple implements Comparable<Apple> {
	private Integer weight;
	private String color;

	public Apple() {};
	
	public Apple(int weight) {
		this.weight = weight;
	}

	public Apple(String color, Integer weight) {
		this.color = color;
		this.weight = weight;
	}
	
	public int getWeight() {
		return weight;
	}

	@Override
	public int compareTo(Apple o) {
		return 0;
	}

}
