package com.vseminar.view

import groovy.transform.TypeChecked

import com.vaadin.data.fieldgroup.BeanFieldGroup
import com.vaadin.data.fieldgroup.FieldGroup.CommitException
import com.vaadin.data.util.BeanItem
import com.vaadin.server.Page
import com.vaadin.server.ThemeResource
import com.vaadin.server.Sizeable.Unit
import com.vaadin.shared.ui.MarginInfo
import com.vaadin.ui.Alignment
import com.vaadin.ui.Button
import com.vaadin.ui.ComboBox
import com.vaadin.ui.Component
import com.vaadin.ui.FormLayout
import com.vaadin.ui.HorizontalLayout
import com.vaadin.ui.Image
import com.vaadin.ui.Notification
import com.vaadin.ui.PasswordField
import com.vaadin.ui.TextField
import com.vaadin.ui.Upload
import com.vaadin.ui.VerticalLayout
import com.vaadin.ui.Button.ClickEvent
import com.vaadin.ui.Notification.Type
import com.vaadin.ui.themes.ValoTheme
import com.vseminar.data.UserData
import com.vseminar.data.UserSession
import com.vseminar.data.model.RoleType
import com.vseminar.data.model.User
import com.vseminar.image.ImageUploader


@TypeChecked
class UserForm extends AbstractForm<User> {
	private TextField name
	private TextField email
	private PasswordField password
	private ComboBox role

	private Button save
	private Button delete
	
	private BeanFieldGroup<User> fieldGroup
	private UserData userData
	private Image image

	UserForm() {
		def root = new VerticalLayout()
		root.addComponent(createContent())
		root.addComponent(createFooter())
		
		setCompositionRoot(root)
		
		this.userData = UserData.instance
		this.fieldGroup = new BeanFieldGroup<User>(User.class)
	}
	
	@Override
	protected void save(ClickEvent e) {
		try {
			fieldGroup.commit()
			def item = fieldGroup.getItemDataSource().getBean()
			def entity = userData.save(item)
			if (UserSession.getUser().id == entity.id) {
				UserSession.setUser(entity)
			}
			getSaveHandler().onSave(entity)
		} catch (CommitException|IllegalArgumentException ex) {
			Notification.show("error while updating profile", ex.getMessage(),
				Type.ERROR_MESSAGE)
		}	
	}
	
	@Override
	protected void delete(ClickEvent e) {
		try {
			def item = fieldGroup.getItemDataSource().getBean()
			userData.delete(item.id)
			getDeleteHandler().onDelete(item)
		} catch (Exception ex) {
			Notification.show("error while deleting profile", ex.getMessage(),
				Type.ERROR_MESSAGE)
		}
	}

	void lazyInit(User user) {
		def item = new User(user)
		fieldGroup.bind(name, "name")
		fieldGroup.bind(email, "email")
		fieldGroup.bind(password, "password")
		fieldGroup.bind(role, "role")
		fieldGroup.bindMemberFields(this)
		fieldGroup.setItemDataSource(new BeanItem<User>(item))
		
//		name.setNullRepresentation("")
//		email.setNullRepresentation("")
//		password.setNullRepresentation("")
//		role.setNullSelectionItemId(RoleType.User)
		
//		name.addValidator(new NullValidator("required name", false))
//		email.addValidator(new EmailValidator("invalid address {0}"))
//		password.addValidator(new NullValidator("required password", false))
		
		email.setEnabled(item.id == null)
		role.setEnabled(UserSession.user.role == RoleType.Admin)
		
		delete.setVisible((item.id != UserSession.user.id) && (item.id != null))
		
		image.setSource(new ThemeResource(item.getImgPath()))
	}
	
	private Component createContent() {
		def content = new HorizontalLayout()
		content.spacing = true
		content.margin = new MarginInfo(true, true, false, true)

		def form = new FormLayout()
		form.setSizeUndefined()
		form.setStyleName(ValoTheme.FORMLAYOUT_LIGHT)
		form.addComponent(name = new TextField("name"))
		form.addComponent(email = new TextField("email"))
		form.addComponent(password = new PasswordField("password"))
		form.addComponent(role = new ComboBox("role"))

		RoleType.values().each {  role.addItem(it) }

		content.addComponent(createUpload())
		content.addComponent(form)
		
		return content
	}

	private Component createUpload() {
		image = new Image()
		image.setWidth(100, Unit.PIXELS)
		image.setSource(new ThemeResource("img/profile-pic-300ppx.jpg"))
		
		final def upload = new Upload()
		upload.setButtonCaption("change")
		upload.setImmediate(true)
		upload.addStyleName(ValoTheme.BUTTON_TINY)
		
		final def imageUploader = new ImageUploader()
		upload.setReceiver(imageUploader)
		upload.addProgressListener({ long readBytes, long contentLength -> 
			def maxLength = 1024 * 500
			if (readBytes > maxLength) {
				upload.interruptUpload()
				new Notification("could not upload file", "file max size:500kb", 
					Notification.Type.ERROR_MESSAGE).show(Page.getCurrent())
			}
		})
		upload.addSucceededListener({ e ->
			if (imageUploader.getSuccessUploadFile() == null) {
				return
			}
			image.setSource(new ThemeResource(imageUploader.getImgPath()))
			fieldGroup.getItemDataSource().getBean().setImgPath(imageUploader.getImgPath())
		})
		
		def imageLayout = new VerticalLayout()
		imageLayout.setSpacing(true)
		imageLayout.setSizeUndefined()
		imageLayout.addComponent(image)
		imageLayout.addComponent(upload)
		
		return imageLayout
	}
	
	private Component createFooter() {
		def footer = new HorizontalLayout()
		footer.setSizeUndefined()
		footer.spacing = true
		footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR)
		footer.setWidth(100, Unit.PERCENTAGE)

		save = new Button("save", { ClickEvent e ->
			save(e)
		} as Button.ClickListener)
		save.addStyleName(ValoTheme.BUTTON_PRIMARY)

		delete = new Button("delete", { ClickEvent e ->
			delete(e)
		} as Button.ClickListener)
		delete.addStyleName(ValoTheme.BUTTON_DANGER)

		footer.addComponents(save, delete)
		footer.setExpandRatio(save, 1)
		footer.setComponentAlignment(save, Alignment.MIDDLE_RIGHT)
		
		save.addClickListener({ ClickEvent e-> 
			save(e) 
		})
		
		delete.addClickListener({ ClickEvent e-> 
			delete(e)
		})

		return footer
	}
}
