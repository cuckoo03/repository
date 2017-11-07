package com.groovy.errortest

import groovy.transform.TypeChecked
import java.util.Set

@TypeChecked
class MyClass1 {
	def MyClass2 myClass2 = new MyClass2()
	
	def void method1() {
		myClass2.getString()

		def objectList = myClass2.getObjectList() 
		
		def mapList = myClass2.getMapList() 
		myClass2.setList(mapList)
		myClass2.setList2(mapList)
	
		def setList = myClass2.getSetList() 
		
		def stringList = myClass2.getStringList()
		myClass2.setStringList(stringList)
		
		stringList.each { String it ->	
			myClass2.setString(it)
		}
		
		mapList.each { Map<String, String> it ->
			myClass2.setMap(it as Map<String, String>)
			myClass2.setMap(new HashMap<>())
			myClass2.setMap(new HashMap())
		}
		
		for (Map<String, String> map in mapList) {
			myClass2.setMap(map)
		}
	}
	
	static void main(String[] args) {
	}
}