package com.groovy.errortest

import groovy.transform.TypeChecked
import java.util.Set

@TypeChecked
class MyClass1 {
	def MyClass2 myClass2 = new MyClass2()
	
	def void method1() {
		myClass2.getString()

		def objectList = myClass2.getObjectList() 
		
		def List<Map> mapList = myClass2.getMapList() as List<Map> 
		myClass2.setList(mapList)
	
		def List<Set> setList = myClass2.getSetList() as List<Set>
		
		def stringList = myClass2.getStringList()
		myClass2.setStringList(stringList)
		
		stringList.each { String it ->	
			myClass2.setString(it)
		}
		
		mapList.each { Map it ->
			myClass2.setMap(it as Map)
			myClass2.setMap(new HashMap())
		}
		
		for (Map map in mapList) {
			myClass2.setMap(map)
		}
	}
	
	static void main(String[] args) {
	}
}
