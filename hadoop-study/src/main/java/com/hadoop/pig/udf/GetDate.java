package com.hadoop.pig.udf;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.Tuple;

/**
 * 접속 일시에서 시간대 정보를 추출하는 함수
 * 
 * @author cuckoo03
 *
 */
public class GetDate extends EvalFunc<String> {
	private SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHH");

	@Override
	public String exec(Tuple input) throws IOException {
		if (input == null || input.size() == 0) {
			return null;
		}
		String date = (String) input.get(0);

		try {
			return sdf2.format(sdf1.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
			throw new IOException(e);
		}
	}
}