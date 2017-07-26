package com.vseminar

import java.util.Date
import com.vaadin.annotations.Theme
import com.vaadin.annotations.VaadinServletConfiguration
import com.vaadin.annotations.Widgetset
import com.vaadin.server.VaadinRequest
import com.vaadin.server.VaadinServlet
import com.vaadin.ui.Button
import com.vaadin.ui.Button.ClickEvent
import com.vaadin.ui.Button.ClickListener
import com.vaadin.ui.Label
import com.vaadin.ui.Notification
import com.vaadin.ui.UI
import com.vaadin.ui.VerticalLayout
import javax.servlet.annotation.WebServlet

/**
 * @author TC
 */
@Theme("vseminar")
@Widgetset("com.vseminar.VSeminarWidgetset")
class VSeminarUI extends UI {
  def init(x$1: VaadinRequest): Unit = {
    val content: VerticalLayout = new VerticalLayout
    setContent(content)

    val label: Label = new Label("Hello, world!")
    content.addComponent(label)

    // Handle user interaction
    content.addComponent(new Button("Click Me from Scala!",
      new ClickListener {
        override def buttonClick(event: ClickEvent): Unit =
          Notification.show("The time is " + new Date)
      }))

    setContent(content)
  }
}

@WebServlet(urlPatterns = Array("/*"), name = "VSeminarUIServlet", asyncSupported = true)
@VaadinServletConfiguration(ui = classOf[VSeminarUI], productionMode = false)
class VSeminarUIServlet extends VaadinServlet {

}