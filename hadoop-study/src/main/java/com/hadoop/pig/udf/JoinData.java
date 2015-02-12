package com.hadoop.pig.udf;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataType;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.apache.pig.impl.logicalLayer.FrontendException;
import org.apache.pig.impl.logicalLayer.schema.Schema;

/**
 * 상품 ID와 상품명 정보를 결합하는 함수
 * 
 * @author cuckoo03
 *
 */
public class JoinData extends EvalFunc<Tuple> {
	private Map<String, String> map = new HashMap<>();
	private TupleFactory factory = TupleFactory.getInstance();

	private String goodsPath;
	private String fileName;

	public JoinData(String p) {
		this.goodsPath = p;
		this.fileName = goodsPath.split("#")[1];
	}

	@Override
	public Tuple exec(Tuple input) throws IOException {
		if (map.size() == 0) {
			Scanner scan = new Scanner(new File(fileName));
			scan.useDelimiter("\n");
			while (scan.hasNext()) {
				String str = scan.next();
				System.out.println(str);
				String[] tmp = str.split(",");
				if (tmp.length == 3) {
					map.put(tmp[0], str);
				}
			}
			if (scan != null) {
				scan.close();
			}
		}
		String key = (String) input.get(0);
		Tuple t = factory.newTuple();
		if (map.containsKey(key)) {
			t.append(key);
			String[] tmp = map.get(key).split(",");
			t.append(tmp[1]);
			t.append(tmp[2]);
			return t;
		}
		return null;
	}

	public List<String> getCacheFiles() {
		List<String> list = new ArrayList<>();
		list.add(goodsPath);
		return list;
	}

	@Override
	public Schema outputSchema(Schema input) {
		try {
			Schema tupleSchema = new Schema();
			tupleSchema.add(new Schema.FieldSchema("id", DataType.CHARARRAY));
			tupleSchema.add(new Schema.FieldSchema("name", DataType.CHARARRAY));
			tupleSchema.add(new Schema.FieldSchema("kananame",
					DataType.CHARARRAY));

			return new Schema(new Schema.FieldSchema(this.getSchemaName(this
					.getClass().getName().toLowerCase(), input), tupleSchema,
					DataType.TUPLE));
		} catch (FrontendException e) {
			e.printStackTrace();
			return null;
		}
	}
}