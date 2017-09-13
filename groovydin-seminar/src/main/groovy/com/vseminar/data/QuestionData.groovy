package com.vseminar.data

import groovy.transform.TypeChecked

import java.util.concurrent.atomic.AtomicLong

import com.vseminar.data.model.Question

@TypeChecked
class QuestionData implements VSeminarData<Question>{
	private static volatile QuestionData INSTANCE = null
	private Map<Long, Question> questions
	private AtomicLong nextId
	private SessionData sessionData
	private QuestionData() {
		sessionData = SessionData.instance	
		nextId = new AtomicLong()
		questions = new LinkedHashMap<>()
	}
	
	@Override
	public Question findOne(long id) {
		def question = questions[id] as Question
		if (question == null) {
			return question
		}
		
		return new Question()
	}

	@Override
	public List<Question> findAll() {
		return Collections.unmodifiableList(new ArrayList<>(questions.values()))
	}

	@Override
	public int count() {
		return questions.size()
	}

	@Override
	public Question save(Question question) {
		if(question.id == null) {
			question.id = nextId.incrementAndGet()
			questions[question.id] = question
			sessionData.addMessage(question)
		}
		
		if (questions.containsKey(question.id)) {
			questions[question.id] = question
			return question
		}
		
		throw new IllegalArgumentException("no question with id $question.id found")
	}

	@Override
	public void delete(long id) {
		def question = findOne(id)
		if (question == null) {
			throw new IllegalArgumentException("question with id $id not found")
		}
		questions.remove(question)
	}
	
	synchronized List<Question> findByIds(Set<Long> ids) {
		def results = new ArrayList<Question>()
		ids.each {
			def question = findOne(it)
			if (question.id != null) {
				results += findOne(it)
			}
		}

		return results
	}

	synchronized static QuestionData getInstance() {
		if (INSTANCE == null) {
			synchronized (QuestionData.class) {
				if (INSTANCE == null) {
					INSTANCE = new QuestionData()
				}
			}
		}
		
		return INSTANCE
	}
}
