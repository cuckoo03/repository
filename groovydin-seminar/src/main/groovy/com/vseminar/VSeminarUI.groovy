package com.vseminar;

import groovy.transform.TypeChecked

import javax.servlet.annotation.WebServlet

import com.vaadin.annotations.Theme
import com.vaadin.annotations.VaadinServletConfiguration
import com.vaadin.annotations.Widgetset
import com.vaadin.server.VaadinRequest
import com.vaadin.server.VaadinServlet
import com.vaadin.ui.Button
import com.vaadin.ui.Label
import com.vaadin.ui.TextField
import com.vaadin.ui.UI
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.Button.ClickListener
import com.vseminar.data.LoadingDataGenerator
import com.vseminar.data.UserSession
import com.vseminar.screen.LoginScreen
import com.vseminar.screen.MainScreen

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@TypeChecked
@Theme("vseminar")
@Widgetset("com.vseminar.VSeminarWidgetset")
public class VSeminarUI extends UI {
	private static final def dataGenerator = new LoadingDataGenerator()

    protected void initbak(VaadinRequest vaadinRequest) {
        final def layout = new VerticalLayout();
        
        final def name = new TextField();
        name.setCaption("Type your name here:my groovy");

		def button = new Button("Click Me", { e ->
			layout.addComponent(new Label("Thanks " + name.getValue()
					+ ", it works!"))
			println ""
		} as Button.ClickListener)
		
        layout.addComponents(name, button);
        layout.setMargin(true);
        layout.setSpacing(true);
        
        setContent(new LoginScreen())
    }
	
	@Override
	protected void init(VaadinRequest vaadinRequest) {
		if (UserSession.isSignedIn()) {
			setContent(new MainScreen(this))
			return
		}
		
		setContent(new LoginScreen())
	}
	
	@WebServlet(urlPatterns = "/*", name = "VSeminarUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = VSeminarUI.class, productionMode = false)
	static class VSeminarUIServlet extends VaadinServlet {
		private static final long serialVersionUID = 1L;
	}
}

