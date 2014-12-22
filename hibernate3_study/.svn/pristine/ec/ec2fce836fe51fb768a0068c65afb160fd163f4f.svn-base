package com.service.hibernate.manytomany;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Semina {
	private Integer seq;
	private Set<Person> persons = new HashSet<Person>();

	public Integer getSeq() {
		return seq;
	}

	private void setSeq(Integer seq) {
		this.seq = seq;
	}

	public Set<Person> getPersons() {
		return persons;
	}

	private void setPersons(Set<Person> persons) {
		this.persons = persons;
	}

	public void addPerson(Person p) {
		this.persons.add(p);
	}
}