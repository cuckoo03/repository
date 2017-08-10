package com.vseminar.data.model

import groovy.transform.Canonical
import groovy.transform.ToString
import groovy.transform.TypeChecked

@TypeChecked
@Canonical
@ToString(includeFields = true, includeNames = true)
class User {
	private static final String DEFAULT_PROFILE_PATH = "img/profile-pic-300px.jpg"
	
	Long id
	String name
	String email
	String password
	String imgPath
	RoleType role

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
