package com.groovy.groovyinaction.ch05

import groovy.transform.TypeChecked;

class Ch05 {
	static main(args) {
		def log = ''
		(1..10).each{ counter -> log += counter }
		println log

		log = ''
		(1..10).each{ log += it}
		println log

		log = ''
		def printer = { log += it}

		MethodClosureSample first = new MethodClosureSample(6)
		MethodClosureSample second = new MethodClosureSample(5)

		Closure firstClosure = first.&validate

		def words = [
			'long string',
			'medium',
			'short',
			'tiny'
		]

		assert 'medium' == words.find(firstClosure)
		println words.findAll(firstClosure)
		assert 'short' == words.find(second.&validate)
		println words.findAll(second.&validate)

		// multimethod
		MultiMethodSample instance = new MultiMethodSample()
		Closure multi = instance.&mysteryMethod

		assert 10 == multi('string arg')
		assert 3 == multi(['list', 'of', 'values'])

		//call closure
		def adder = {x, y -> return x + y }
		assert adder(4, 3) == 7
		assert adder.call(4, 3) == 7

		// implement curry b y closure
		adder = {x, y -> return x + y}
		def addOne = adder.curry(1)
		println addOne(5)

		// is case
		assert [1, 2, 3].grep { it < 3} == [1, 2]

		// range of closure
		Mother julia = new Mother()
		Closure closure = julia.birth(4)
		def context = closure.call(this)
		println context[0].class.name
		assert context[1..4] == [1, 2, 3, 4]
		println context[5] instanceof Script
		println context[6] instanceof Mother

		firstClosure = julia.birth(4)
		def secondClosure = julia.birth(4)
		println firstClosure.isCase(secondClosure)
		
		// apply visitor patttern
		def picture = new Drawing(shapes:[new Square(width:3), 
			new Circle(radius:3)])
		
		def total = 0
		picture.accept { total += it.area() }
		picture.accept { println it.class.name + ":" + it.area() }
	}
}
class MethodClosureSample {
	int limit
	MethodClosureSample(int limit) {
		this.limit = limit
	}
	boolean validate(String value) {
		return value.length() <= limit
	}
}
class MultiMethodSample {
	int mysteryMethod(String value) {
		return value.length()
	}
	int mysteryMethod(List list) {
		return list.size()
	}
	int mysteryMethod(int x, int y) {
		return x + y
	}
}
class Mother {
	int field = 1
	int foo() {
		return 2
	}
	Closure birth (param) {
		def local = 3
		def closure = { caller ->
			[ this, field, foo(), local, param, caller, owner ]
		}
		return closure
	}
}
class Drawing {
	List shapes
	def accept(Closure yield) { shapes.each { it.accept(yield)} }
}
class Shape {
	def accept(Closure yield) { yield(this) }
}
class Square extends Shape {
	def width
	def area() {
		width **2
	}
}
class Circle extends Shape {
	def radius
	def area() {
		Math.PI * radius ** 2 
	}
}