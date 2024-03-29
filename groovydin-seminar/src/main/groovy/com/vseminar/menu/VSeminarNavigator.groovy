package com.vseminar.menu

import groovy.transform.TypeChecked

import com.vaadin.navigator.Navigator
import com.vaadin.ui.ComponentContainer
import com.vaadin.ui.UI
import com.vseminar.data.UserSession
import com.vseminar.data.model.RoleType;

@TypeChecked
class VSeminarNavigator extends Navigator {
	static final def naviMaps = new LinkedHashMap<String, Navi>()
	private Map<String, Navi> activeNaviMaps

	VSeminarNavigator(UI ui, ComponentContainer container) {
		super(ui, container)
		
		final def userRoleType = UserSession.user.role
		
		activeNaviMaps = new LinkedHashMap<>()
		naviMaps.each { key, Navi value ->
			if (userRoleType.ordinal() >= value.roleType.ordinal()) {
				super.addView(value.fragment, value.viewClass)
				activeNaviMaps[value.fragment] = value
			}
		}
	}
	
	Map<String, Navi> getActiveNaviMaps() {
		return activeNaviMaps
	}
}
