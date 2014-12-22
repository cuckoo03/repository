package com.service.hibernate.manytomany;

import java.util.HashSet;
import java.util.Set;

public class Person {
	private Integer seq;
	private Set<Semina> seminars = new HashSet<Semina>();

	public void addSemina(Semina s) {
		this.seminars.add(s);
	}

	public void cancelSemina(Semina s) {
		this.seminars.remove(s);
	}

	public Integer getSeq() {
		return seq;
	}

	private void setSeq(Integer seq) {
		this.seq = seq;
	}

	public Set<Semina> getSeminars() {
		return seminars;
	}

	public void setSeminars(Set<Semina> seminars) {
		this.seminars = seminars;
	}
}
