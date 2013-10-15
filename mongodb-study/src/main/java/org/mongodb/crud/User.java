package org.mongodb.crud;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Document(collection="user")
public class User {
	@Id
	private String id;
	private String name;
	private int age;

	public User(String name, int age) {
		this.name = name;
		this.age = age;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "id:" + id + " name:" + name + " age:" + age;
	}
}
