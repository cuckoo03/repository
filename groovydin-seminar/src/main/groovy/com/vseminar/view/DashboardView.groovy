package com.vseminar.view

import groovy.transform.TypeChecked

import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent
import com.vaadin.server.Sizeable.Unit
import com.vaadin.ui.Alignment
import com.vaadin.ui.Component
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Label
import com.vaadin.ui.TabSheet
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.themes.ValoTheme

@TypeChecked
class DashboardView extends VerticalLayout implements View {
	static final String VIEW_NAME = ""

	DashboardView() {
		setHeight(100, Unit.PERCENTAGE)
		def createContent = createContent()
		addComponent(createTopBar())	
		addComponent(createContent)
		setExpandRatio(createContent, 1)
	}

	@Override
	void enter(ViewChangeEvent event) {
		
	}
	
	HorizontalLayout createTopBar() {
		def title = new Label("Dashboard")
		title.setSizeUndefined()
		title.addStyleName(ValoTheme.LABEL_H1)
		title.addStyleName(ValoTheme.LABEL_NO_MARGIN)
		
		def topLayout = new HorizontalLayout()
		topLayout.setSpacing(true)
		topLayout.setWidth(100, Unit.PERCENTAGE)
		topLayout.addComponent(title)
		topLayout.setComponentAlignment(title, Alignment.MIDDLE_LEFT)
		topLayout.addStyleName("top-bar")

		return topLayout
	}
	
	private Component createContent() {
		def contentLayout = new HorizontalLayout()
		contentLayout.setSizeFull()
		contentLayout.setSpacing(true)
		
		def sessionLayout = new VerticalLayout()
		sessionLayout.setSizeFull()
		sessionLayout.addComponent(createSessionTab())

		return contentLayout
	}
	
	private TabSheet createSessionTab() {
		return null
	}
}
