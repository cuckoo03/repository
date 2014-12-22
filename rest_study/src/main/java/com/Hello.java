package com;

import javax.xml.bind.annotation.XmlRootElement;

// json 변환시 key가 되는 필드
@XmlRootElement(name="hello")
public class Hello {
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
