package com.storm.cookbook.ch01.click;

import java.io.Serializable;

import org.json.simple.JSONObject;

public class HttpIpResolver implements IPResolver, Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * url로 접속하여 받아온 정보를 json로 변환하여 반환
	 */
	@Override
	public JSONObject resolveIp(String ip) {
		// TODO Auto-generated method stub
		return null;
	}

}
