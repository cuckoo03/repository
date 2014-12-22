package com.service.hibernate.onetomany;

import java.util.HashSet;
import java.util.Set;

public class Customer {
	private String id;
	private String password;
	private String name;
	private String tel;
	private Set<Support> supports = new HashSet<Support>();

	public Customer() {
	}

	public void addSupport(Support support) {
		if (null == getSupports()) {
			setSupports(new HashSet<Support>());
		}
		getSupports().add(support);
		support.setCustomer(this);
	}
	
	public void delSupport(Support support) {
		getSupports().remove(support);
	}
	
	public void clearSupports() {
		getSupports().clear();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public Set<Support> getSupports() {
		return supports;
	}

	private void setSupports(Set<Support> supports) {
		this.supports = supports;
	}
}