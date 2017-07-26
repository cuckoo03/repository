package com.vseminar;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import com.vseminar.screen.LoginScreen;

/**
 * This UI is the application entry point. A UI may either represent a browser
 * window (or tab) or some part of a html page where a Vaadin application is
 * embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is
 * intended to be overridden to add component to the user interface and
 * initialize non-component functionality.
 */
@Theme("vseminar")
@Widgetset("com.vseminar.VSeminarWidgetset")
public class VSeminarUI extends UI {
	private static final long serialVersionUID = 1L;

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		/*
		final VerticalLayout layout = new VerticalLayout();

		final TextField name = new TextField();
		name.setCaption("Type your name here:");

		Button button = new Button("Click Me1");
		button.addClickListener(e -> {
			layout.addComponent(new Label("Thanks " + name.getValue()
					+ ", it works!"));
		});

		layout.addComponents(name, button);
		layout.setMargin(true);
		layout.setSpacing(true);

		setContent(layout);
		*/
		
		this.setContent(new LoginScreen());
	}

	@WebServlet(urlPatterns = "/*", name = "VSeminarUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = VSeminarUI.class, productionMode = false, resourceCacheTime = 36, heartbeatInterval = 3, closeIdleSessions = false)
	public static class VSeminarUIServlet extends VaadinServlet {
		private static final long serialVersionUID = 1L;
	}
}
