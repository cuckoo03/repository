package com.corejava.chap001;

public class Exam1_5UnsynchBankTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Bank b = new Bank(NACCOUNTS, INITIAL_BALANCE);
		int i;
		for (i = 0; i < NACCOUNTS; i++) {
			TransferThread t = new TransferThread(b, i, INITIAL_BALANCE);
			t.setPriority(Thread.NORM_PRIORITY + i % 2);
			t.start();
		}
	}

	public static final int NACCOUNTS = 1000;
	public static final int INITIAL_BALANCE = 14000;

}

class Bank {
	public Bank(int n, int initialBalance) {
		accounts = new int[n];
		int i;
		for (i = 0; i < accounts.length; i++)
			accounts[i] = initialBalance;
		ntransacts = 0;
	}

	public synchronized void transfer(int from, int to, int amount) {

		// if (accounts[from] < amount)
		// return;
		// accounts[from] -= amount;
		// accounts[to] += amount;
		// ntransacts++;
		// if (ntransacts % NTEST == 0)
		// test();

		try {
			while (accounts[from] < amount)
				wait();
			accounts[to] += amount;
			accounts[from] -= amount;
			ntransacts++;
			notifyAll();
			if (ntransacts % NTEST == 0)
				test();
		} catch (InterruptedException e) {
		}
	}

	public void test() {
		int sum = 0;
		for (int i = 0; i < accounts.length; i++)
			sum += accounts[i];
		System.out.println("Transactions:" + ntransacts + " Sum: " + sum);
	}

	public int size() {
		return accounts.length;
	}

	public static final int NTEST = 10000;
	private final int[] accounts;
	private long ntransacts = 0;
}

class TransferThread extends Thread {
	public TransferThread(Bank b, int from, int max) {
		bank = b;
		fromAccount = from;
		maxAmount = max;
	}

	public void run() {
		try {
			while (!interrupted()) {
				int toAccount = (int) (bank.size() * Math.random());
				int amount = (int) (maxAmount * Math.random());
				bank.transfer(fromAccount, toAccount, amount);
				sleep(1);
			}
		} catch (InterruptedException e) {
		}
	}

	private Bank bank;
	private int fromAccount;
	private int maxAmount;
}