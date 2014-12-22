package com.service.hibernate.manytomany2;

import java.util.HashSet;
import java.util.Set;

public class Category {
	private Integer id;
	private Set items = new HashSet();

	public void addItem(Item item) {
		this.items.add(item);
	}

	public Integer getId() {
		return id;
	}

	private void setId(Integer id) {
		this.id = id;
	}

	public Set getItems() {
		return items;
	}

	public void setItems(Set items) {
		this.items = items;
	}
}
