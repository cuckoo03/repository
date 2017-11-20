package com.java.inaction.ch08.template;


public abstract class OnlineBanking {
	public void processCustomer(int id) {
		Customer c = new Customer(id);
		makeCustomer(c);
	}

	abstract void makeCustomer(Customer c);
}
