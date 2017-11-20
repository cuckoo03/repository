package com.java.inaction.ch08.template;

import java.util.function.Consumer;

public class OnlineBankingLambda {
	public void processCustomer(int id, Consumer<Customer> consumer) {
		consumer.accept(new Customer(id));
	}
}
