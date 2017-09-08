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
}
