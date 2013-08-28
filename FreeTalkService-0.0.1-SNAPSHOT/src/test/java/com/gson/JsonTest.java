package com.gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

public class JsonTest {
	@Test
	public void test() {
		List<String> list = new ArrayList<String>();
		list.add("a");
		list.add("b");

		JSONObject toJson = new JSONObject();
		try {
			toJson.put("list", list);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		ObjectMapper mapper = new ObjectMapper();
		try {
			String fromJson = mapper.writeValueAsString(list);
			list = mapper.readValue(fromJson,
					new TypeReference<List<String>>() {
					});
			for (String s : list) {
				System.out.println(s);
			}
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
