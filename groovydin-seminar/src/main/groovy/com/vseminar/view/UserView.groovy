package com.vseminar.view

import groovy.transform.TypeChecked

import com.vaadin.data.util.BeanItemContainer
import com.vaadin.event.ItemClickEvent
import com.vaadin.navigator.View
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent
import com.vaadin.server.FontAwesome
import com.vaadin.server.Page
import com.vaadin.server.Sizeable.Unit
import com.vaadin.ui.Alignment
import com.vaadin.ui.Button
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Label
import com.vaadin.ui.Table
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.themes.ValoTheme
import com.vseminar.data.UserData
import com.vseminar.data.UserSession
import com.vseminar.data.model.User

@TypeChecked
class UserView extends VerticalLayout implements View {
	private static final String VIEW_NAME = "user"
	private BeanItemContainer<User> container
	private UserData userData
	
	private UserForm userForm
	
	UserView() {
		userData = UserData.getInstance()

		setHeight(100, Unit.PERCENTAGE)
		def table = createTable()
		findBean()

		addComponent(createTopBar())
		addComponent(table)
		setExpandRatio(table, 1)
		
		createForm()
	}

	@Override
	void enter(ViewChangeEvent event) {
	}

	HorizontalLayout createTopBar() {
		def title = new Label("User")
		title.setSizeUndefined()
		title.addStyleName(ValoTheme.LABEL_H1)
		title.addStyleName(ValoTheme.LABEL_NO_MARGIN)

		def topLayout = new HorizontalLayout()
		topLayout.setSpacing(true)
		topLayout.setWidth(100, Unit.PERCENTAGE)
		topLayout.addComponent(title)
		topLayout.setComponentAlignment(title, Alignment.MIDDLE_LEFT)

		def newBtn = new Button("new", { e -> 
			userForm.lazyInit(new User())
			userForm.openPopup("new user")
		} as Button.ClickListener)		
		newBtn.addStyleName(ValoTheme.BUTTON_PRIMARY)
		newBtn.setIcon(FontAwesome.PLUS_CIRCLE)
		
		topLayout.addComponent(newBtn)
//		topLayout.setComponentAlignment(title, Alignment.MIDDLE_LEFT)
		topLayout.setExpandRatio(title, 1)
		
		return topLayout
	}
	
	private Table createTable() {
		def table = new Table()
		table.setSizeFull()
		
		container = new BeanItemContainer<>(User.class)
		table.setContainerDataSource(container)
		
		/*
		table.addGeneratedColumn("picture", { Table t, Object itemId, Object columnInd ->
			def user = (User) itemId
			def image = new ThemeResource(user.getImgPath())
			
			return new Image(null, image)
		})
		*/
		table.setVisibleColumns("imgPath", "name", "email", "password", "role")
		table.setColumnHeaders("img", "name", "email", "password", "role")
//		table.setColumnWidth("imgPath", 60)
		
		table.addItemClickListener({ ItemClickEvent e -> 
			userForm.lazyInit((User) e.getItemId())
			userForm.openPopup("edit profile")
		})

		return table
	}
	
	private void findBean() {
		def users = userData.findAll()
		if (users.size() > 0) {
			container.removeAllItems()
		}
		container.addAll(users)
	}
	
	private void createForm() {
		userForm = new UserForm()	
		userForm.setSaveHandler(new SaveHandler<User>() {
			@Override
			void onSave(User entity) {
				userForm.closePopup()
				if (UserSession.getUser().getId() == entity.getId()) {
					Page.getCurrent().reload()
					return
				}
				findBean()
			}
		})
		userForm.setDeleteHandler(new DeleteHandler<User>() {
			@Override
			void onDelete(User entity) {
				userForm.closePopup()
				findBean()
			}
		})
	}
}