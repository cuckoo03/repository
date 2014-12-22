package com.thread.introduction01;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class ListI1_13Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ThreadFactory factory = Executors.defaultThreadFactory();
		Bank bank = new Bank("mybank", 1000);
		factory.newThread(new BankThread(bank)).start();
		factory.newThread(new BankThread(bank)).start();
		factory.newThread(new BankThread(bank)).start();
		factory.newThread(new BankThread(bank)).start();
		factory.newThread(new BankThread(bank)).start();
		factory.newThread(new BankThread(bank)).start();
		factory.newThread(new BankThread(bank)).start();
		factory.newThread(new BankThread(bank)).start();
		factory.newThread(new BankThread(bank)).start();
		factory.newThread(new BankThread(bank)).start();
	}
}
