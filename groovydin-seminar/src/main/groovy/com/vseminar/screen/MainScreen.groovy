package com.vseminar.screen

import groovy.transform.TypeChecked

import com.vaadin.navigator.Navigator
import com.vaadin.ui.Button
import com.vaadin.ui.CssLayout
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Label
import com.vaadin.ui.UI
import com.vseminar.data.UserSession
import com.vseminar.view.AboutView
import com.vseminar.view.DashboardView
import com.vseminar.view.SessionView
import com.vseminar.view.UserView

@TypeChecked
class MainScreen extends HorizontalLayout {
	MainScreen(UI ui) {
		def label = new Label(UserSession.user.email)
		final def signout = new Button("sign out", { e ->
			UserSession.signout()
		} as Button.ClickListener)
		
		addComponent(label)
		addComponent(signout)

		def viewContainer = new CssLayout()
		
		def final navigator = new Navigator(ui, viewContainer)		
		
		navigator.addView(DashboardView.VIEW_NAME, new DashboardView())
		navigator.addView(SessionView.VIEW_NAME, new SessionView())
		navigator.addView(AboutView.VIEW_NAME, new AboutView())
		navigator.addView(UserView.VIEW_NAME, new UserView())
		
		addComponent(viewContainer)
		
		println "state:$UI.current.navigator.state"
		navigator.navigateTo(UI.current.navigator.state)
	}
}
