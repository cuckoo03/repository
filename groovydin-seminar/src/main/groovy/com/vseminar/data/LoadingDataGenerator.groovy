package com.vseminar.data

import groovy.transform.TypeChecked

import com.vaadin.server.FontAwesome
import com.vseminar.data.model.RoleType
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
	}

	private static void createUsers() {
		def userData = UserData.instance
		userData.save(new User("u1", "u1@", "1", "img/upload/1.jpg", RoleType.User))
		userData.save(new User("u2", "u2@", "1", "img/upload/2.jpg", RoleType.Admin))
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
}
