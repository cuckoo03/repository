package com.thread.ch02.exam02_15;

public final class MutablePerson {
	private String name;
	private String address;

	public MutablePerson(String name, String address) {
		this.name = name;
		this.address = address;
	}

	public MutablePerson(ImmutablePerson person) {
		this.name = person.getName();
		this.address = person.getAddress();
	}

	public synchronized void setPerson(String newName, String newAddress) {
		this.name = newName;
		this.address = newAddress;
	}

	public synchronized ImmutablePerson getImmutablePerson() {
		return new ImmutablePerson(this);
	}

	String getAddress() {
		return address;
	}

	String getName() {
		return name;
	}

	public synchronized String toString() {
		return "[MutablePerson: " + name + ", " + address + "]";
	}
}
