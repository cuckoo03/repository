package com.groovy.groovyinaction.ch09

import com.sun.org.apache.bcel.internal.classfile.Deprecated;

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
//		obj.invokeMethod("third", 2)
//		println obj.third
		
		// object methods
		def a = new Integer(1)
		def b = new Integer(1)
		def c = a
		int a1 = 1
		int b1 = 1
		int c1 = a1
		// object.is() : 리터럴값은값비교, 참조형은 참조값비교
		assert false == a.is(b)
		assert true == a.is(c)
		assert true == a1.is(b1)
		assert true == a1.is(c1)
		
		// == : 값비교
		assert true == (a == b)
		assert true == (a == c)
		assert true == (a1 == b1)
		assert true == (a1 == c1)
		
		println System.identityHashCode(a)
		println System.identityHashCode(b)
		println System.identityHashCode(c)
		
		c.sleep(200);
		println "sleeped"
		
		new Date().identity  {
			println "$date.$month"
		}
		
		def list = new int[2]
		list[0] = 11
		list[1] = 22
		list.each {
			println it
		}
		println ""
		
		def map = new HashMap<>()
		map.put('a', 1)
		map['b'] =  2
		map.each{key, value ->
			println "$key:$value"
		}
		
		map.findAll {
			println it
		}
		
		list.findAll { value ->
			println "$value"
		}
		
		// use File class
		def file = new File("")
		println file.isFile()
		
		
	}
}
class MyClass {
	def first = 1
	def getSecond() {
		first * 2
	}
	public third = 3
	@Override
	public String toString() {
		return "1"
	}
}
