package com.vseminar.data

import groovy.transform.TypeChecked

import com.vaadin.server.FontAwesome
import com.vseminar.data.model.LevelType
import com.vseminar.data.model.Question
import com.vseminar.data.model.RoleType
import com.vseminar.data.model.Session
import com.vseminar.data.model.User
import com.vseminar.menu.Navi
import com.vseminar.menu.VSeminarNavigator
import com.vseminar.view.DashboardView
import com.vseminar.view.SessionView
import com.vseminar.view.UserView

@TypeChecked
class LoadingDataGenerator {
	static {
		createUsers()
		createNavis()
		createSessions()
	}

	private static void createUsers() {
		def userData = UserData.instance
		userData.save(new User("u1", "u1@", "1", "img/upload/1.jpg", RoleType.User))
		userData.save(new User("u2", "u2@", "2", "img/upload/2.jpg", RoleType.Admin))
	}

	private static void createNavis() {
		VSeminarNavigator.naviMaps[""] = new Navi(fragment:DashboardView.VIEW_NAME,
		viewName:"Dashboard", viewClass:DashboardView.class, icon:FontAwesome.HOME,
		roleType:RoleType.User)

		VSeminarNavigator.naviMaps["session"] = new Navi(fragment:SessionView.VIEW_NAME,
		viewName:"Session", viewClass:SessionView.class, icon:FontAwesome.CUBE,
		roleType:RoleType.User)
		
		VSeminarNavigator.naviMaps["user"] = new Navi(fragment:UserView.VIEW_NAME,
			viewName:"User", viewClass:UserView.class, icon:FontAwesome.CUBE,
			roleType:RoleType.User)
	}
	
	private static void createSessions() {
		def userData = UserData.getInstance()
		def user1Id = userData.findOne(1L).id
		def user2Id = userData.findOne(2L).id
		
		def sessionData = SessionData.getInstance()
		
		sessionData.save(new Session("title1", LevelType.Junior, "url1", 
			"speaker1", user1Id, ""))
		sessionData.save(new Session("title2", LevelType.Senior, "url2", 
			"speaker2", user2Id, ""))
	}
	
	private static void createQuestions(Session session, Long userId) {
		def questionData = QuestionData.instance
		(1..30).each {
			questionData.save(new Question(session.id, "test sample $it:$session.title", userId))
		}
	}
}
