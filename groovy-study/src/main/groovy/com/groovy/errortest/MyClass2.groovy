package com.groovy.errortest

import groovy.transform.CompileStatic;
import groovy.transform.TypeChecked;

@TypeChecked
//@CompileStatic
class MyClass2 {
	
	def void setString(String str) {
		
	}
	
	def String getString() {
		return "string"
	}
	
	def List<Object> getObjectList() {
		return null
	}
	
	def List<Map<String, String>> getMapList() {
		return null
	}
	
	def List<Set> getSetList() {
		return null
	}
	
	def List<String> getStringList() {
		return null
	}

	def void setList(List<Map> list) {
		println "list" 
	}	
	
	def void setList2(List<Map<String, String>> list) {
		println "list"
	}
	
	def void setStringList(List<String> list) {
		println "stringList"
	}
	
	def void setMap(Map<String, String> map) {
		println map
	}
}
