package com.service.hibernate.onetomany;

import java.util.HashSet;
import java.util.Set;

public class Support {
	private int id;
	private String title;
	private String contents;
	private Customer customer;
	private Set<Reply> replys = new HashSet<Reply>();

	public void addReply(Reply reply) {
		if (null == getReplys()) {
			setReplys(new HashSet<Reply>());
		}

		getReplys().add(reply);
		reply.setSupport(this);
	}

	public void delReply(Reply reply) {
		getReplys().remove(reply);
	}

	public void clear() {
		getReplys().clear();
	}

	public int getId() {
		return id;
	}

	@SuppressWarnings("unused")
	private void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Set<Reply> getReplys() {
		return replys;
	}

	private void setReplys(Set<Reply> replys) {
		this.replys = replys;
	}
}
