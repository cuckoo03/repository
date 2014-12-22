package com.thread.introduction01;

public class BankThread implements Runnable {
	private Bank bank;

	public BankThread(Bank bank) {
		this.bank = bank;
	}

	public void run() {
		while (true) {
			boolean ok = bank.withdraw(1000);
			if (ok) {
				bank.deposit(1000);
			}
		}
	}
}
