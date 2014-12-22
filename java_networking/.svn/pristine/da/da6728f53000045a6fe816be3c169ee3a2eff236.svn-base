package com.thread.ch12_exam12_2;

import java.math.BigInteger;

public class Servant implements ActiveObject {
	public Result<String> add(String x, String y) {
		String retvalue = null;
		try {
			BigInteger xx = new BigInteger(x);
			BigInteger yy = new BigInteger(y);
			BigInteger zz = xx.add(yy);
			retvalue = zz.toString();
		} catch (NumberFormatException e) {
			retvalue = null;
		}
		return new RealResult<String>(retvalue);
	}
}
