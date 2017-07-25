package com.vseminar.screen;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class LoginScreen extends VerticalLayout {
	private static final long serialVersionUID = 1L;

	public LoginScreen() {
		this.setSizeFull();
		Component loginForm = buildForm();
		this.addComponent(loginForm);
		this.setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);
	}

	private Component buildForm() {
		final VerticalLayout loginPanel = new VerticalLayout();

		loginPanel.setSizeUndefined();
		loginPanel.setSpacing(true);
		loginPanel.addComponent(buildLabel());
		loginPanel.addComponent(buildFields());

		return loginPanel;
	}

	private Component buildFields() {
		final TextField email = new TextField("Email");
		email.setIcon(FontAwesome.USER);
		email.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

		final PasswordField password = new PasswordField("Password");
		password.setIcon(FontAwesome.LOCK);
		password.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

		final Button signin = new Button("sign in");
		signin.addStyleName(ValoTheme.BUTTON_PRIMARY);
		signin.focus();

		HorizontalLayout fields = new HorizontalLayout();
		fields.setSpacing(true);
		fields.addComponents(email, password, signin);
		fields.setComponentAlignment(signin, Alignment.BOTTOM_LEFT);
		
		return fields;
	}

	private Component buildLabel() {
		Label titleLabel = new Label("welcome");
		titleLabel.addStyleName(ValoTheme.LABEL_H4);
		titleLabel.addStyleName(ValoTheme.LABEL_COLORED);

		return titleLabel;
	}
}
