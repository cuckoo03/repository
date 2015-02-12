package com.hadoop.pig.udf;

import java.io.IOException;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.Tuple;

/**
 * 연령대를 식별하는 함수
 * 
 * @author cuckoo03
 *
 */
public class CheckAge extends EvalFunc<Integer> {
	@Override
	public Integer exec(Tuple input) throws IOException {
		if (input == null || input.size() == 0) {
			return null;
		}
		Integer age = (Integer) input.get(0);

		if (age < 20) {
			return 1;
		} else if (age >= 60) {
			return 3;
		} else {
			return 2;
		}
	}
}