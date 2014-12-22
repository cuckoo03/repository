package com.thread.ch12_list12_1;

public interface ActiveObject {
	public abstract Result<String> makeString(int i, char fillchar);

	public abstract void displayString(String string);
}
