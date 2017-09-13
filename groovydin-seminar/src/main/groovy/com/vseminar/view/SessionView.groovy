package com.vseminar.view

import groovy.transform.TypeChecked

import com.vaadin.data.fieldgroup.FieldGroup.CommitEvent
import com.vaadin.data.fieldgroup.FieldGroup.CommitException
import com.vaadin.data.fieldgroup.FieldGroup.CommitHandler
import com.vaadin.data.sort.Sort
import com.vaadin.data.util.BeanItemContainer
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent
import com.vaadin.server.FontAwesome
import com.vaadin.server.Sizeable.Unit
import com.vaadin.shared.data.sort.SortDirection
import com.vaadin.shared.ui.datefield.Resolution
import com.vaadin.ui.Alignment
import com.vaadin.ui.Button
import com.vaadin.ui.DateField
import com.vaadin.ui.Grid
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Label
import com.vaadin.ui.TextField
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.themes.ValoTheme
import com.vseminar.data.SessionData
import com.vseminar.data.UserSession
import com.vseminar.data.model.RoleType
import com.vseminar.data.model.Session

@TypeChecked
class SessionView extends VerticalLayout implements View {
	static final String VIEW_NAME = "session"
	private Grid grid
	private BeanItemContainer<Session> container
	private SessionData sessionData
	private Button newBtn
	private Button delBtn

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

		newBtn = new Button("new") 
		newBtn.addStyleName(ValoTheme.BUTTON_PRIMARY)
		newBtn.setIcon(FontAwesome.PLUS_CIRCLE)
		newBtn.addClickListener({ event -> 
			container.addItemAt(0, new Session(UserSession.getUser().id))	
			grid.scrollToStart()
		} as Button.ClickListener)
		topLayout.addComponent(newBtn)
		
		delBtn = new Button("del")
		delBtn.addStyleName(ValoTheme.BUTTON_DANGER)
		delBtn.setIcon(FontAwesome.MINUS_CIRCLE)
		delBtn.setEnabled(false)
		delBtn.addClickListener({ event ->
			def selection = grid.getSelectionModel()
			for (Object itemId : selection.getSelectedRows()) {
				def session = (Session) itemId
				if (session.id) {
					sessionData.delete(session.id)
				}
				grid.getContainerDataSource().removeItem(session)
			}
			grid.getSelectionModel().reset()
			delBtn.setEnabled(false)
		} as Button.ClickListener)
		topLayout.addComponent(delBtn)

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
		grid.getColumn("endDate").setEditorField(dateEditorField())
		grid.getColumn("description").setHeaderCaption("DESC").setEditorField(textEditorField())
		
		grid.getEditorFieldGroup().addCommitHandler(new CommitHandler() {
			@Override
			void preCommit(CommitEvent event) throws CommitException {
				
			}
			
			@Override
			void postCommit(CommitEvent event) throws CommitException {
				def session = (Session) grid.getEditedItemId()
				sessionData.save(session)
			}
		})

		grid.setSelectionMode(SelectionMode.MULTI)
		grid.addSelectionListener({ event ->
			delBtn.setEnabled(grid.getSelectedRows().size() > 0)
		})
		
		grid.getColumn("questions").setHidable(true).setEditable(false)
	
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
	
	private DateField dateEditorField() {
		def dateField = new DateField()
		dateField.setResolution(Resolution.MINUTE)
		
		return dateField
	}
	
	private TextField textEditorField() {
		def textField = new TextField()
		textField.setNullRepresentation("null")

		return textField
	}
}
