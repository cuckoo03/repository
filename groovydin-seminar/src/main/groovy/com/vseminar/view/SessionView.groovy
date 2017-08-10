package com.vseminar.view

import groovy.transform.TypeChecked

import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent
import com.vaadin.server.Sizeable.Unit
import com.vaadin.ui.Alignment
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Label
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.themes.ValoTheme

@TypeChecked
class SessionView extends VerticalLayout implements View {
	static final String VIEW_NAME = "session"

	SessionView() {
		addComponent(createTopBar())	
	}

	@Override
	void enter(ViewChangeEvent event) {
		
	}
	
	HorizontalLayout createTopBar() {
		def title = new Label("Session")
		title.setSizeUndefined()
		title.addStyleName(ValoTheme.LABEL_H1)
		title.addStyleName(ValoTheme.LABEL_NO_MARGIN)
		
		def topLayout = new HorizontalLayout()
		topLayout.setSpacing(true)
		topLayout.setWidth(100, Unit.PERCENTAGE)
		topLayout.addComponent(title)
		topLayout.setComponentAlignment(title, Alignment.MIDDLE_LEFT)

		return topLayout
	}
}
