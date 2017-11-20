package com.java.inaction.ch08;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import com.java.inaction.ch08.chain.ProcessingObject;
import com.java.inaction.ch08.observer.ConcreteSubject;
import com.java.inaction.ch08.observer.Observer;
import com.java.inaction.ch08.observer.ObserverA;
import com.java.inaction.ch08.observer.ObserverB;
import com.java.inaction.ch08.template.Customer;
import com.java.inaction.ch08.template.OnlineBankingLambda;

public class Ch08Main {
	final static Map<String, Supplier<Product>> map = new HashMap<>();
	
	public static void doSome(Runnable r) {
		r.run();
	}

	public static void doSome(Task t) {
		t.execute();
	}

	public static Product createProduct(String name) {
		Supplier<Product> p = map.get(name);
		if (p != null) {
			return p.get();
		}
		throw new IllegalArgumentException();
	}
	public static void main(String[] args) {
		// refactoring anonymouse class to lambda
		Runnable r = () -> System.out.println("H");
		r.run();

		int a = 10;
		Runnable r1 = () -> {
			// int a = 2;
		};
		r1.run();

		Runnable r2 = new Runnable() {
			@Override
			public void run() {
				int a = 2;
				System.out.println(a);
			}
		};
		r2.run();

		doSome(new Task() {
			@Override
			public void execute() {
				System.err.println("danger");
			}
		});

		doSome((Task) () -> System.err.println("danger2"));

		// strategy pattern
		Validator numValidator = new Validator(new IsNumeric());
		boolean b1 = numValidator.validate("aaa");
		Validator lowerValidator = new Validator(new IsAllowCase());
		boolean b2 = lowerValidator.validate("bbb");
		System.out.println(b1);
		System.out.println(b2);
		
		numValidator = new Validator((s) -> s.matches(""));
		System.out.println(numValidator.validate("aaa"));
		
		// template pattern
		new OnlineBankingLambda().processCustomer(1,
				(Customer c) -> System.out.println("hello " + c.getId()));
		
		//observer
		ConcreteSubject s = new ConcreteSubject();
		s.register(new ObserverA());
		s.register((String t) -> {
			if (t.contains("b")) {
				System.out.println("b observer");
			}
		}); 
		s.notify("a");
		s.notify("b");

		// chain pattern
		ProcessingObject<String> p1 = new HeaderProcessing();
		ProcessingObject<String> p2 = new FooterProcessing();
		p1.setSuccessor(p2);
		
		String result = p1.handle("start");
		System.out.println(result);
		
		UnaryOperator<String> headerP = (String t) -> "header:" + t;
		UnaryOperator<String> footerP = (String t) -> "footer:" + t;
		Function<String, String> pipeline = headerP.andThen(footerP);
		System.out.println(pipeline.apply("start"));
		
		// factory pattern
		map.put("loan", Loan::new);
		map.put("Stock", Stock::new);
		System.out.println(createProduct("loan"));
		
		

	}
}
interface Product {
	
}
class Loan implements Product {
	
}
class Stock implements Product {
	
}