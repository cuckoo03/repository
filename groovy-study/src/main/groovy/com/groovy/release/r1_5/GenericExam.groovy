package com.groovy.release.r1_5

import groovy.transform.TypeChecked;

class Talk {
	String title
}
class Speaker {
	String name
	List<Talk> talks = []
}
@TypeChecked
class GenericExam {
	static main(args) {
		def me = new Speaker(name:"A", talks:[new Talk(title:"aa"), new Talk(title:"bb")])
		def talkField = me.class.getDeclaredField("talks")
		assert talkField.genericType.toString() == "java.util.List<Talk>"
	}
}
