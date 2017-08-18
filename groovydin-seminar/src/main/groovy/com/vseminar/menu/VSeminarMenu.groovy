package com.vseminar.menu

import groovy.transform.TypeChecked

import com.vaadin.server.FontAwesome
import com.vaadin.server.Page
import com.vaadin.server.ThemeResource
import com.vaadin.shared.ui.label.ContentMode
import com.vaadin.ui.Alignment
import com.vaadin.ui.Button
import com.vaadin.ui.CssLayout
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Label
import com.vaadin.ui.MenuBar
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.themes.ValoTheme
import com.vseminar.data.UserSession
import com.vseminar.data.model.RoleType
import com.vseminar.data.model.User
import com.vseminar.view.DashboardView
import com.vseminar.view.SaveHandler
import com.vseminar.view.UserForm

@TypeChecked
class VSeminarMenu extends CssLayout {
	private static final String VALO_MENU_VISIBLE = "valo-menu-visible"
	private static final String VALO_MENU_TOGGLE = "valo-menu-toggle"
	private static final String VALO_MENUITEMS = "valo-menuitems"
	
	private CssLayout menuPart
	private CssLayout menuItems

	VSeminarMenu(final VSeminarNavigator navigator) {
		setPrimaryStyleName(ValoTheme.MENU_ROOT)

		menuPart = new CssLayout()
		menuPart.addStyleName(ValoTheme.MENU_PART)

		final def title = new Label("vaddin seminar", ContentMode.HTML)
		title.setSizeUndefined()

		final def top = new HorizontalLayout()
		top.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT)
		top.addStyleName(ValoTheme.MENU_TITLE)
		top.setSpacing(true)
		top.addComponent(title)

		menuPart.addComponent(top)

		final def userMenu = new MenuBar()
		userMenu.addStyleName("user-menu")
		menuPart.addComponent(userMenu)

		def image = new ThemeResource(UserSession.user.imgPath)
		final def userMenuItem = userMenu.addItem(UserSession.user.name, image, null)
		userMenuItem.addItem("edit profile", new MenuBar.Command() {
			@Override
			void menuSelected(final MenuItem selectedItem) {
				final def userForm = new UserForm()
				userForm.lazyInit(UserSession.getUser())
				userForm.openPopup("edit profile")
				
				userForm.setSaveHandler(new SaveHandler<User>() {
					@Override
					void onSave(User user) {
						userForm.closePopup()
						Page.getCurrent().reload()
					}
				})
			}
		})
		
		userMenuItem.addItem("sign out", { e ->
			UserSession.signout()	
		})
		
		final def showMenu = new Button("menu", { e ->
			if (menuPart.styleName.contains(VALO_MENU_VISIBLE)) {
				menuPart.removeStyleName(VALO_MENU_VISIBLE)
			} else {
				menuPart.addStyleName(VALO_MENU_VISIBLE)
			}
		} as Button.ClickListener)
		
		showMenu.addStyleName(ValoTheme.BUTTON_PRIMARY)
		showMenu.addStyleName(ValoTheme.BUTTON_SMALL)
		showMenu.addStyleName(VALO_MENU_TOGGLE)
		showMenu.setIcon(FontAwesome.NAVICON)
		
		menuPart.addComponent(showMenu)
		
		menuItems = new CssLayout()
		menuItems.setPrimaryStyleName(VALO_MENUITEMS)
		
		menuPart.addComponent(menuItems)
		
		addComponent(menuPart)
		
		RoleType sectionType = null
		navigator.getActiveNaviMaps().each { String key, Navi value ->
			final def fragmenet = value.fragment
			final def viewName = value.viewName
			final def viewClass = value.viewClass
			final def icon = value.icon
			final def roleType = value.roleType
			if (viewClass != DashboardView.class && sectionType != roleType) {
				sectionType = roleType
				def label = new Label("Role_" + sectionType.name(), ContentMode.HTML)
//				label.setPrimaryStyleName(ValoTheme.MENU_SUBTITLE)
//				label.addStyleName("h4")
				label.setSizeUndefined()
				
				menuItems.addComponent(label)
			}
			
			final def naviBtn = new Button(viewName, { e-> 
				navigator.navigateTo(fragmenet)
			} as Button.ClickListener)
			naviBtn.setData(fragmenet)
			naviBtn.setPrimaryStyleName(ValoTheme.MENU_ITEM)
//			naviBtn.setIcon(icon)
			
			menuItems.addComponent(naviBtn)
		}
	}
}
