package com.vseminar.data.model

import groovy.transform.Canonical;
import groovy.transform.ToString
import groovy.transform.TypeChecked

@TypeChecked
@ToString(includeFields = true, includeNames = true)
class User {
	private static final String DEFAULT_PROFILE_PATH = "img/profile-pic-300px.jpg"
	
	Long id
	String name
	String email
	String password
	String imgPath
	RoleType role
	
	/*
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public RoleType getRole() {
		return role;
	}

	public void setRole(RoleType role) {
		this.role = role;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	*/

	User() {
		this.role = RoleType.User
	}
	
	User(User other) {
		this.id = other.id
		this.name = other.name
		this.email = other.email
		this.password = other.password
		this.imgPath = other.imgPath
		this.role = other.role
	}
	
	User(String name, String email) {
		this.name = name
		this.email = email
		this.role = RoleType.User
	}
	
	User(String name, String email, String password, String imgPath, RoleType role) {
		this.name = name
		this.email = email
		this.password = password
		this.imgPath = imgPath
		this.role = role
	}
	
	String getImgPath() {
		return imgPath == null ? DEFAULT_PROFILE_PATH : imgPath
	}
}
