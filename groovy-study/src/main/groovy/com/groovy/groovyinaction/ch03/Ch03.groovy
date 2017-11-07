package com.groovy.groovyinaction.ch03

import groovy.transform.TypeChecked;

@TypeChecked
class Ch03 {
	static main(args) {
		// translation strings
		def a = "A"
		def b = "B"
		def c = "a:$a b:$b"
		assert c == "a:A b:B"
		
		def date = new Date()
		
		def greeting = "Hello groovy!"
		assert greeting.startsWith("Hello")
		assert greeting.getAt(0) == "H"
		assert greeting[0] == "H"
		
		assert "x".padLeft(3) == "  x"
		
		greeting = 'Hello'
		greeting <<= ' Groovy'
		println greeting
		assert greeting << '!'
		println greeting
//		greeting[1..4] = "i"
		println greeting
		
		// use regular expression
		assert 'abc' == /abc/
		assert "\\d" == /\d/
		
		def reference = "hello"
		assert reference == /$reference/
		assert "\$" == /$/
		
		// use number
		println 1f*2f
		println 1*2L
		println 1/2
		
		def store = ''
		10.times {
			store += 'x'
		}
		println store;
		
		0.times {
			println '0 times'
		}
		
		store = ''
		1.upto(5) { number ->
			store += number
		}
		println store
		
		store = ''
		2.downto(-2) { number ->
			store += number + ' '
		}
		println store
		
		store = ''
		0.step(0.5, 0.1) { number ->
			store += number.toString() + ' ' 
		}
		println store;
	}
}
