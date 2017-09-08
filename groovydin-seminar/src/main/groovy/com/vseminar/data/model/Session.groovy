package com.vseminar.data.model

import groovy.transform.ToString
import groovy.transform.TypeChecked

@TypeChecked
@ToString(includeFields = true, includeNames = true)
class Session {
	private static final int MAX_ENTRIES = 100
	private Set<Long> questions

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
		
//		newMessages()
	}
	
	Session(Long ownerId) {
		this.ownerId = ownerId
		this.level = LevelType.Junior
		this.startDate = new Date()
		this.endDate = new Date()

//		newMessages()
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

//		newMessages()
	}
		
	private void newMessages() {
		def initialCapacity = MAX_ENTRIES + 1
		def loadFactor = 0.90F
		def accessOrder = false
		def hashmap = new LinkedHashMap<Long, Boolean>(initialCapacity, loadFactor, accessOrder) {
			protected boolean removeEldestEntry(Map.Entry<Long, Boolean> eldest) {
				return false
			}
		}
		this.questions = Collections.newSetFromMap(hashmap)
	}
}
