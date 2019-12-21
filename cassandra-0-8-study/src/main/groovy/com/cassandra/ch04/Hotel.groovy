package com.cassandra.ch04

import groovy.transform.Immutable
import groovy.transform.ToString
import groovy.transform.TypeChecked

@TypeChecked
@ToString
@Immutable
class Hotel {
	private String id
	private String name
	private String phone
	private String address
	private String city
	private String state
	private String zip
}
