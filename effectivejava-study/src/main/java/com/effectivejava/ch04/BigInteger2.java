package com.effectivejava.ch04;

import java.math.BigInteger;

@SuppressWarnings("serial")
public class BigInteger2 extends BigInteger {
	public BigInteger2(String val) {
		super(val);
	}

	public void foo(BigInteger b) {
		if (b.getClass() != BigInteger.class) {
			b = new BigInteger(b.toByteArray());
			System.out.println("Biginteger value:" + b);
		}
	}

	public static void main(String[] args) {
		BigInteger bint = new BigInteger("123456789");
		BigInteger2 bint2 = new BigInteger2("123456789");
		bint2.foo(bint);
		bint2.foo(bint2);
	}

}
