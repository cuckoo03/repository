package com.groovy.inaction.ch04

import static java.util.stream.Collectors.*;

import groovy.transform.TypeChecked

import com.java.inaction.ch04.Dish

@TypeChecked
class Chapter04 {
	static main(args) {
		def menu = [
			new Dish("pork", false, 800, Dish.Type.MEAT),
			new Dish("french", true, 700, Dish.Type.OTHER)
		]

		def names = menu.stream().filter{it.calories > 100}.map{it.name}
		.limit(3).collect(toList())
		println names

		names = menu.stream().filter{
			println "filtering:$it.name";
			it.cal > 100
		}.map{
			println "mapping:$it.name";
			it.name
		}.limit(3).collect(toList())
		println names
	}
}
