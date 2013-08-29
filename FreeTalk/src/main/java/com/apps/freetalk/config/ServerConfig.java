package com.apps.freetalk.config;

public class ServerConfig {
	public static final String SERVER_URL = "http://54.250.217.195:8080/FreeTalkService";
//	public static final String SERVER_URL = "http://192.168.0.3:8080/FreeTalkService";
	public static final String PROJECT_ID = "785873295611";
	private static final int CONN_TIMEOUT = 30;
	private static final int READ_TIMEOUT = 30;

	public static int getConnTimeout() {
		return CONN_TIMEOUT;
	}

	public static int getReadTimeout() {
		return READ_TIMEOUT;
	}
}
