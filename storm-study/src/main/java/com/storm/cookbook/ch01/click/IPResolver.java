package com.storm.cookbook.ch01.click;

import org.json.simple.JSONObject;

public interface IPResolver {
	JSONObject resolveIp(String ip);
}
