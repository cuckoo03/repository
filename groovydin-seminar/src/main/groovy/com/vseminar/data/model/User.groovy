package com.vseminar.data.model

import groovy.transform.TypeChecked;

@TypeChecked
class User {
	private static final String DEFAULT_PROFILE_PATH = "img/profile-pic-300px.jpg"
	
	def Long id
	def String name
	def String email
	def String password
	def String imgPath
	def RoleType role

	public User() {
		this.role = RoleType.User
	}
	
	public User(User other) {
		this.id = other.id
	}
}
