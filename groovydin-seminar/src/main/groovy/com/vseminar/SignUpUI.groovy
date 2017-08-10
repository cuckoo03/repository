package com.vseminar

import groovy.transform.TypeChecked

import javax.servlet.annotation.WebServlet

import com.vaadin.annotations.VaadinServletConfiguration
import com.vaadin.server.VaadinRequest
import com.vaadin.server.VaadinServlet
import com.vaadin.ui.Label
import com.vaadin.ui.UI

@TypeChecked
class SignUpUI extends UI {
	@Override
	protected void init(VaadinRequest request) {
		setContent(new Label("signup"))
	}

	@WebServlet(urlPatterns = "/signup", name = "SignUpUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = SignUpUI.class, productionMode = false)
	static class SignUpServlet extends VaadinServlet {
	}
}
