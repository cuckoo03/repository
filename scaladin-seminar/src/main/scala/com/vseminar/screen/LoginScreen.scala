package com.vseminar.screen

import com.vaadin.event.ShortcutAction.KeyCode
import com.vaadin.server.FontAwesome
import com.vaadin.ui.Alignment
import com.vaadin.ui.Button
import com.vaadin.ui.Button.ClickListener
import com.vaadin.ui.Component
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Label
import com.vaadin.ui.PasswordField
import com.vaadin.ui.TextField
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.themes.ValoTheme
import com.vaadin.ui.Notification
import java.util.Date
import com.vaadin.ui.Button.ClickEvent

/**
 * @author TC
 */
class LoginScreen extends VerticalLayout {
  // constructor
  setSizeFull()
  val loginForm = buildForm()
  addComponent(loginForm)
  setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER)

  def buildForm(): Component = {
    val loginPanel = new VerticalLayout()

    loginPanel.setSizeUndefined()
    loginPanel.setSpacing(true)
    loginPanel.addComponent(buildLabels())
    loginPanel.addComponent(buildFields())

    loginPanel
  }

  def buildLabels(): Component = {
    val titleLabel = new Label("welcome")
    titleLabel.addStyleName(ValoTheme.LABEL_H4)
    titleLabel.addStyleName(ValoTheme.LABEL_COLORED)

    titleLabel
  }

  def buildFields(): Component = {
    val email = new TextField("Email")
    email.setIcon(FontAwesome.USER)
    email.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON)

    val password = new PasswordField("Password")
    password.setIcon(FontAwesome.LOCK)
    password.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON)

    val signin = new Button("sign in")
    signin.addStyleName(ValoTheme.BUTTON_PRIMARY)
    signin.focus()
    signin.setClickShortcut(KeyCode.ENTER)
    signin.addClickListener(new ClickListener {
      override def buttonClick(event: ClickEvent): Unit =
        Notification.show(s"${email.getValue}, ${password.getValue}")
    })

    val fields = new HorizontalLayout()
    fields.setSpacing(true)
    fields.addComponents(email, password, signin)
    fields.setComponentAlignment(signin, Alignment.BOTTOM_LEFT)

    fields
  }
}