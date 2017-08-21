package com.vseminar.data

import groovy.transform.TypeChecked

import java.util.concurrent.atomic.AtomicLong

import com.vseminar.data.model.Session
import com.vseminar.data.model.User

@TypeChecked
class SessionData implements VSeminarData<Session> {
	private static volatile SessionData INSTANCE = null
	private Map<Long, Session> sessions
	private AtomicLong nextId
	
	private SessionData() {
		this.nextId = new AtomicLong()
		this.sessions = new LinkedHashMap<>()
	}

	synchronized static SessionData getInstance() {
		if (!INSTANCE) {
			synchronized (SessionData.class) {
				if (INSTANCE == null) {
					INSTANCE = new SessionData()
				}
				
			}
		}
		
		return INSTANCE
	}

	@Override
	public Session findOne(long id) {
		def session = sessions[id]
		if (session) {
			return session
		}
		
		return new Session()
	}

	@Override
	public List<Session> findAll() {
		return Collections.unmodifiableList(new ArrayList<Session>(sessions.values()))
	}

	@Override
	public int count() {
		return sessions.size()
	}

	@Override
	public Session save(Session session) {
		def checkSessions = findByTitle(session.title)
		if (!session.id) {
			if (checkSessions.size() > 0) {
				throw new IllegalArgumentException("duplicated session name")
			}
			session.id = nextId.incrementAndGet()
			sessions[session.id] = session
			
			return session 
		} 
		
		if (sessions.containsKey(session.id)) {
			def equalFirstItemId = session.id != checkSessions.get(0).id
			def equalFirstItemTitle = session.title == checkSessions.get(0).title  
			if (equalFirstItemId && equalFirstItemTitle) {
				throw new IllegalArgumentException("duplicated session title")
			}
			sessions[session.id] = session
			
			return session
		}
		
		throw new IllegalArgumentException("no session with id ${session.id} found")
	}

	@Override
	public void delete(long id) {
		def session = findOne(id)
		if (!session) {
			throw new IllegalArgumentException("session with id $id not found")
		}
		sessions.remove(session.id)
	}

	synchronized List<Session> findByTitle(String title) {
		def sessions = findAll()
		def results = sessions.findAll { it.title == title } as List<Session>
		
		return results 
	}
	
	synchronized List<Session> findByOwner(User user) {
		def List<Session> sessions = findAll()
		def results = sessions.findAll { it.ownerId == user.id } as List<Session>
		
		return results 
	}
}
