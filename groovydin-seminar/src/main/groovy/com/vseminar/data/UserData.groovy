package com.vseminar.data

import groovy.transform.TypeChecked

import java.util.concurrent.atomic.AtomicLong

import com.vseminar.data.model.User

@TypeChecked
class UserData implements VSeminarData<User> {
	private static volatile UserData INSTANCE = null
	private Map<Long, User> users
	private AtomicLong nextId

	private UserData() {
		this.nextId = new AtomicLong()
		users = new LinkedHashMap<>()
	}

	synchronized static UserData getInstance() {
		if (INSTANCE == null) {
			synchronized (UserData.class) {
				if (INSTANCE == null) {
					INSTANCE = new UserData()	
				}
			}
		}	
		
		return INSTANCE
	}

	@Override
	synchronized User findOne(long id) {
		def user = users.get(id)
		if (user != null) {
			return user
		}
		
		new User()
	}

	@Override
	List<User> findAll() {
		return Collections.unmodifiableList(new ArrayList<User>(users.values()))
	}

	@Override
	int count() {
		return users.size()
	}

	@Override
	synchronized User save(User user) {
		def checkUser
		if (user.id == null) {
			checkUser = findByNameOrEmail(user.name, user.email)
			if (checkUser.id != null) {
				throw new IllegalArgumentException("duplicated username or email")
			}

			user.id = nextId.incrementAndGet()
			users[user.id] = user
			
			return user
		}

		checkUser = findByName(user.name)
		if (users.containsKey(user.id)) {
			if ((user.id != checkUser.id) && (user.name == checkUser.name)) {
				throw new IllegalArgumentException("duplicated user name")
			}

			users[user.id] = user
			
			return user
		}
		
		throw new IllegalArgumentException("no user with id ${user.id} found")
	}

	@Override
	void delete(long id) {
		def user = findOne(id)
		if (user == null) {
			throw new IllegalArgumentException("user with id ${id} not found")
		}

		users.remove(user.id)
	}
	
	synchronized User findByName(String name) {
		def users = findAll()
		users.each { 
			if (it.name == name) {
				return it 
			}
		}

		return new User()
	}
	
	synchronized User findByNameOrEmail(String name, String email) {
		def users = findAll()
		users.each {
			if (it.name == name || it.email == email) {
				return it
			}
		}

		return new User()
	}
	
	synchronized User findByEmailAndPassword(String email, String password) {
		def users = findAll()
		for (user in users) {
			if (user.email == email && user.password == password) {
				println "found email:${email}"
				return user
			}
		} 
		
		return new User()
	}
}
