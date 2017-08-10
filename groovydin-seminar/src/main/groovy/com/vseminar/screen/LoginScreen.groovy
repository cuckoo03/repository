package com.vseminar.screen

import groovy.transform.TypeChecked

import com.vaadin.event.ShortcutAction.KeyCode
import com.vaadin.server.FontAwesome
import com.vaadin.server.Page
import com.vaadin.ui.Alignment
import com.vaadin.ui.Button
import com.vaadin.ui.Component
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Label
import com.vaadin.ui.Notification
import com.vaadin.ui.PasswordField
import com.vaadin.ui.TextField
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.themes.ValoTheme
import com.vseminar.data.UserNotFoundException
import com.vseminar.data.UserSession
import com.vseminar.validator.CustomValidator

@TypeChecked
class LoginScreen extends VerticalLayout {
	UserSession userSession
	
	LoginScreen() {
		setSizeFull()
		def loginForm = buildForm()
		addComponent(loginForm)
		setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER)
		
		this.userSession = new UserSession()
	}
	
	Component buildForm() {
		def final loginPanel = new VerticalLayout()  
		loginPanel.setSizeUndefined()
		loginPanel.setSpacing(true)
		loginPanel.addComponent(buildLabels())
		loginPanel.addComponent(buildFields())

		return loginPanel
	}
	
	Component buildLabels() {
		def titleLabel = new Label("welcomde")
		titleLabel.addStyleName(ValoTheme.LABEL_H4)
		titleLabel.addStyleName(ValoTheme.LABEL_COLORED)

		return titleLabel
	}
	
	Component buildFields() {
		def final email = new TextField("Email")
		email.setIcon(FontAwesome.USER)
		email.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON)
//		email.addValidator(new EmailValidator("invalid email address {0}"))
		email.addValidator(new CustomValidator())
		
		def final password = new PasswordField("Password")
		password.setIcon(FontAwesome.LOCK)
		password.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON)
		
		def final signin = new Button("sign in")
		signin.addStyleName(ValoTheme.BUTTON_PRIMARY)
		signin.focus()
		signin.setClickShortcut(KeyCode.ENTER)
		signin.addClickListener({ e ->
			try {
				userSession.signin(email.value, password.value)
				Page.getCurrent().reload()
			} catch (UserNotFoundException ex) {
				Notification.show( "user not found", 
					Notification.Type.ERROR_MESSAGE)
				ex.printStackTrace()
			}
		})
		
		def fields = new HorizontalLayout()
		fields.setSpacing(true)
		fields.addComponents(email, password, signin) 
		fields.setComponentAlignment(signin, Alignment.BOTTOM_LEFT)
		
		return fields
	}
}
