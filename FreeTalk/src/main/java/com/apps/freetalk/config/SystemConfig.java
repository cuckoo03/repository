package com.apps.freetalk.config;

public class SystemConfig {
	private static String userId;

	public static String getUserId() {
		return userId;
	}

	public static void setUserId(String userId) {
		SystemConfig.userId = userId;
	}

	private static String password;
	public static String REGISTRATION_ID = "";

	public static String getPassword() {
		return password;
	}

	public static void setPassword(String password) {
		SystemConfig.password = password;
	}
}
