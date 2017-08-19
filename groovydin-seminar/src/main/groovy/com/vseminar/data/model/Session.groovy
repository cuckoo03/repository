package com.vseminar.data.model

import groovy.transform.ToString
import groovy.transform.TypeChecked

@TypeChecked
@ToString(includeFields = true, includeNames = true)
class Session {
	Long id
	String title
	LevelType level
	String embeddedUrl
	Date startDate
	Date endDate
	Long ownerId
	String speaker
	String description
	
	Session() {
		this.level = LevelType.Junior
		this.startDate = new Date()
		this.endDate = new Date()
	}
	
	Session(Long ownerId) {
		this.ownerId = ownerId
		this.level = LevelType.Junior
		this.startDate = new Date()
		this.endDate = new Date()
	}
	
	Session(String title, LevelType level, String embeddedUrl, String speaker, 
		Long ownerId, String descrption) {
		this.title = title
		this.level = level
		this.embeddedUrl = embeddedUrl
		this.speaker = speaker
		this.ownerId = ownerId
		this.startDate = new Date()
		this.endDate = new Date()
		this.description = descrption
	}
}
