package com.vseminar.data.model

import groovy.transform.ToString
import groovy.transform.TypeChecked

@TypeChecked
@ToString(includeFields = true, includeNames = true)
class Question {
	Long id
	Long sessionId
	String message
	Long createdBy
	Date createdDate
	
	Question() {
		this.createdDate = new Date()
	}
	
	Question(Long id, String message, Long createdBy) {
		this.id = id
		this.message = message
		this.createdBy = createdBy
	}
}
