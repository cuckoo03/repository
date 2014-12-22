package com.thread.ch02.exam02_15;

public class Main {
	public static void main(String[] args) {
		String name = "alice";
		String address = "korea";
		ImmutablePerson immutablePerson = new ImmutablePerson(name, address);
		MutablePerson mutablePerson = new MutablePerson(immutablePerson);
		
		System.out.println(immutablePerson.toString());
		System.out.println(mutablePerson.toString());
		
		ImmutablePerson immun1 = mutablePerson.getImmutablePerson();
		immun1.getMutablePerson().setPerson("bobby", "america");
		
		System.out.println(immun1.toString());
	}
}
