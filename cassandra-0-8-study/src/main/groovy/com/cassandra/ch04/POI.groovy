package com.cassandra.ch04

import groovy.transform.Immutable
import groovy.transform.ToString
import groovy.transform.TypeChecked

@TypeChecked
@ToString
@Immutable
class POI {
	private String name
	private String desc
	private String phone
}
