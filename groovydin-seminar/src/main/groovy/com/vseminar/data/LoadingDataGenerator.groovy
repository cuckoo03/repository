package com.vseminar.data

import groovy.transform.TypeChecked

import com.vseminar.data.model.RoleType
import com.vseminar.data.model.User

@TypeChecked
class LoadingDataGenerator {
	static {
		createUsers()
	}
	
	static void createUsers() {
		def userData = UserData.instance
		userData.save(new User("u1", "u1@", "1", "img/upload/1.jpg", RoleType.User))
		userData.save(new User("u2", "u2@", "1", "img/upload/2.jpg", RoleType.Admin))
	}
}
