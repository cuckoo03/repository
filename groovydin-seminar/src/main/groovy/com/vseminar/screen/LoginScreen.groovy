package com.vseminar.screen

import groovy.transform.TypeChecked

import com.vaadin.event.ShortcutAction.KeyCode
import com.vaadin.server.FontAwesome
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

@TypeChecked
class LoginScreen extends VerticalLayout {
	LoginScreen() {
		setSizeFull()
		def loginForm = buildForm()
		addComponent(loginForm)
		setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER)
	}
	
	def Component buildForm() {
		def final loginPanel = new VerticalLayout()  
		loginPanel.setSizeUndefined()
		loginPanel.setSpacing(true)
		loginPanel.addComponent(buildLabels())
		loginPanel.addComponent(buildFields())

		loginPanel
	}
	
	def Component buildLabels() {
		def titleLabel = new Label("welcomde")
		titleLabel.addStyleName(ValoTheme.LABEL_H4)
		titleLabel.addStyleName(ValoTheme.LABEL_COLORED)

		titleLabel
	}
	
	def Component buildFields() {
		def final email = new TextField("Email")
		email.setIcon(FontAwesome.USER)
		email.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON)
		
		def final password = new PasswordField("Password")
		password.setIcon(FontAwesome.LOCK)
		password.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON)
		
		def final signin = new Button("sign in")
		signin.addStyleName(ValoTheme.BUTTON_PRIMARY)
		signin.focus()
		signin.setClickShortcut(KeyCode.ENTER)
		signin.addClickListener({ e ->
			Notification.show("${email.value}, ${password.value}")
		})
		
		def fields = new HorizontalLayout()
		fields.setSpacing(true)
		fields.addComponents(email, password, signin) 
		fields.setComponentAlignment(signin, Alignment.BOTTOM_LEFT)
		
		fields
	}
}
