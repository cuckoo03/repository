package org.jaxb;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "persons")
public class Persons {
	private List<NewPerson> newPerson;

	public List<NewPerson> getNewPerson() {
		return newPerson;
	}

	public void setNewPerson(List<NewPerson> persons) {
		this.newPerson = persons;
	}
}
