package com.thread.introduction01;

public class Bank {
	private int money;
	private String name;

	public Bank(String name, int money) {
		this.name = name;
		this.money = money;
	}

	public void deposit(int m) {
		money += m;
	}

	public Boolean withdraw(int m) {
		if (money >= m) {
			money -= m;
			check();
			return true;
		} else {
			return false;
		}
	}

	public String getName() {
		return name;
	}

	private void check() {
		if (money < 0) {
			System.out.println("마이너스 입니다 money = " + money);
		}
	}
}
