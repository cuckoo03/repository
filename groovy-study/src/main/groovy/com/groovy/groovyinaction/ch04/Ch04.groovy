package com.groovy.groovyinaction.ch04

import groovy.transform.TypeChecked;

@TypeChecked
class Ch04 {
	static main(args) {
		// use complex type
		// use range
		assert (0..10).contains(10)
		assert (0..10).contains(-1) == false
		assert (0..<10).contains(9)

		def a = 0..10
		assert a instanceof Range
		a = new IntRange(0, 10)
		assert a.contains(5)

		def today = new Date()
		def yesterday = today - 1
		println today
		yesterday = today.previous()
		println yesterday
		assert (yesterday..today).size() == 2

		def log = ''
		for (element in 5..9) {
			log += element
		}
		println log

		log = ''
		(9..<5).each{ element -> log += element }
		println log

		def age = 10
		switch (age) {
			case 1..5:println "1..5";break
			case 5..9:println "5..9";break
			default: println "default"
		}

		def ages = [20, 36, 42, 56]
		def midage = 21..50
		assert ages.grep(midage) == [36, 42]

		// define list
		def myList = [1, 2, 3]
		assert myList instanceof List

		def explicitList = new LinkedList()
		explicitList.addAll(myList)
		assert explicitList.size() == 3
		assert explicitList[2] == 3

		// access list
		myList = ['a', 'b', 'c', 'd']
		assert myList[0..2] == ['a', 'b', 'c']
		myList[1..2] = ['bb', 'cc']
		assert myList == ['a', 'bb', 'cc', 'd']

		// add, delete list
		myList = []
		myList += 'a'
		assert myList == ['a']

		myList = []
		myList << 'a' << 'b'
		assert myList == ['a', 'b']
		assert myList - ['b']== ['a']


		myList = []
		if (myList) {
			assert false
		}

		log = ''
		for (i in [1, 'x', 5]) {
			log += i;
		}
		println log

		// modify list
		def list = []
		assert [1, [2, 3]].flatten() == [1, 2, 3]
		assert [1, 2, 3].intersect([4, 3, 1]) == [3, 1]
		assert [1, 2, 3].disjoint([4, 5, 6])

		list = [1, 2, 3]
		list.pop();
		assert list == [1, 2]

		assert list.reverse() == [2, 1]
		assert list.sort() == [1, 2]

		list = ['a', 'b', 'c']
		list.remove(2)
		assert list == ['a', 'b']

		list = list.sort{it -> println it[0] }

		def doubled = [1, 2, 3].collect{ item-> item*2 }
		assert doubled == [2, 4, 6]

		def x= [1, 1, 1]
		assert [1]== new HashSet(x).toList();

		// retrive, reverse, sum
		list = [1, 2, 3]
		assert list.max() == 3

		def even = list.find { item ->
			item % 2 == 0
		}
		assert even == 2

		assert list.every { item -> item < 4 }
		assert list.any { item -> item < 4 }
		
		assert list.join('-') == '1-2-3'
		
		def result = list.inject(0) { clinks, guests ->
			clinks += guests
		}
		assert result == 0+1+2+3
		assert list.sum() == 6
		println result
		println list
		
		// use map
		def myMap = [a:1, b:2, c:3]
		assert myMap instanceof HashMap
		assert myMap.size() == 3
		assert myMap['a'] == 1
		
		def emptyMap = [:]
		assert emptyMap.size() == 0
		
		def explicitMap = new TreeMap();
		explicitMap.putAll(myMap)
		assert explicitMap['a'] == 1
		
		//use map
		assert myMap.a == 1
		assert myMap.get('a') == 1
		assert myMap.get('b', 0) == 2
		assert myMap["d"] == null
		assert myMap.d == null
		
		myMap.d = 4;
		assert myMap.d == 4
		
		//retrieve map element
		assert myMap.isEmpty() == false
		assert myMap.size() == 4
		assert myMap.containsKey("a") == true
		
		println myMap.keySet()
		println myMap.values()
		println myMap.entrySet()
		assert myMap.any { entry -> entry.value > 2 }
		assert myMap.every { entry -> entry.key < 'e' }
		
		def store = ''
		myMap.each  {entry ->
			store += entry.key
			store += entry.value
		}
		println store
		
		store = ''
		myMap.each {key, value ->
			store += key
			store += value
		}
		println store
		
		// change map element
//		myMap.clear();
//		assert myMap.isEmpty();
		
		def abMap = myMap.subMap(['a', 'b'])
		println abMap
		
		myMap = [a:1, b:2, c:3]
		def found = myMap.find { entry -> entry.value < 3}
		println "found:" + found
		
		doubled = myMap.collect { entry -> entry.value.multiply(2) }
		assert doubled instanceof List
		
		def textCorpus = "Look for the bare necessities" 
		
		def words = textCorpus.tokenize();
		def Map<String, Integer> wordFrequency = [:]
		words.each  { String word ->
			wordFrequency[word] = wordFrequency.getOrDefault(word, 0) + 1
		}
		println wordFrequency
		def wordList = wordFrequency.keySet().toList();
		wordList.sort { wordFrequency[it] }
		
		def statistics = "\n"
		wordList.each { word ->
			statistics += word.padLeft(12) + ":"
			statistics += wordFrequency[word] + "\n"
		}
		println statistics
	}
}

