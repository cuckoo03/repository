package com.hadoop.definitiveguide.ch11;

import java.io.IOException;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.Tuple;

public class CustomPigEvalFunc extends EvalFunc {

	@Override
	public Object exec(Tuple input) throws IOException {
		return null;
	}

}
