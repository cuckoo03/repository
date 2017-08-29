package com.vseminar.view

import com.vaadin.data.sort.Sort
import com.vaadin.data.util.BeanItemContainer
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent
import com.vaadin.server.Sizeable.Unit
import com.vaadin.shared.data.sort.SortDirection
import com.vaadin.ui.Alignment
import com.vaadin.ui.Grid
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Label
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.themes.ValoTheme
import com.vseminar.data.SessionData
import com.vseminar.data.UserSession
import com.vseminar.data.model.RoleType
import com.vseminar.data.model.Session

import groovy.transform.TypeChecked

@TypeChecked
class SessionView extends VerticalLayout implements View {
	static final String VIEW_NAME = "session"
	private Grid grid
	private BeanItemContainer<Session> container
	private SessionData sessionData

	SessionView() {
		sessionData = SessionData.getInstance()

		setHeight(100, Unit.PERCENTAGE)
		grid = createGrid()
		findBean()

		addComponent(createTopBar())	
		addComponent(grid)
		setExpandRatio(grid, 1)
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
	
	private Grid createGrid() {
		def grid = new Grid()
		grid.setSizeFull()
//		grid.setEditorEnabled(true)
		
		container = new BeanItemContainer<>(Session.class, null)
		grid.setContainerDataSource(container)
		
		grid.setColumnOrder("id", "title", "level", "startDate", "endDate", 
			"embeddedUrl", "speaker", "description")
		
		grid.getColumn("id").setHeaderCaption("ID1")
		grid.getColumn("title").setHeaderCaption("TITLE").setHidden(true)
	
		return grid 
	}
	
	private void findBean() {
		def sessions = new ArrayList<Session>()
		if (UserSession.getUser().role == RoleType.Admin) {
			sessions += sessionData.findAll()
		} else {
			sessions += sessionData.findByOwner(UserSession.getUser())
		}
		
		if (sessions.size <= 0) {
			return
		}
		
		container.removeAllItems()
		container.addAll(sessions)
		
		grid.sort(Sort.by("startDate", SortDirection.ASCENDING))
		
	}
}
