package com.groovy.groovyinaction.ch09

class Ch09 {
	static main(args) {
		def newline = "\n"
		assert newline.toString() == "\n"
		println newline.dump()
		assert newline.inspect() == /'\n'/
		
		//property reflection
		def obj = new MyClass()
		def keys = ['first', 'second', 'class']
		assert obj.properties.keySet() == new HashSet(keys)
		
		assert 1 == obj.properties['first']
		assert 2 == obj.properties.second
	}
}
class MyClass {
	def first = 1
	def getSecond() {
		first * 2
	}
	public third = 3
}
