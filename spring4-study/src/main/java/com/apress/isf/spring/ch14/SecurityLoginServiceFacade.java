package com.apress.isf.spring.ch14;


public class SecurityLoginServiceFacade {
	private Login login;

	public void setLogin(Login login) {
		this.login = login;
	}
	public void print() {
		System.out.println(login);
	}
}
