package com.vseminar.view

import groovy.transform.TypeChecked

import com.vaadin.ui.Button
import com.vaadin.ui.CustomComponent
import com.vaadin.ui.Window
import com.vaadin.ui.UI

@TypeChecked
abstract class AbstractForm<T> extends CustomComponent {
	private Window window

	private SaveHandler<T> saveHandler
	private DeleteHandler<T> deleteHandler
	
	protected abstract void save(Button.ClickEvent e)
	protected abstract void delete(Button.ClickEvent e)
	
	SaveHandler<T> getSaveHandler() {
		return saveHandler
	}
	
	DeleteHandler<T> getDeleteHandler() {
		return deleteHandler
	}

	void setSaveHandler(SaveHandler<T> saveHandler) {
		this.saveHandler = saveHandler
	}
	
	void setDeleteHandler(DeleteHandler<T> deleteHandler) {
		this.deleteHandler = deleteHandler
	}
	
	Window openPopup(String title) {
		window = new Window(title, this)
		window.setModal(true)
		window.setResizable(true)
		window.center()
		// must import UI class
		UI.getCurrent().addWindow(window)

		return window
	}
	
	void closePopup() {
		if (!window) {
			window.close()
			window = null
		}
	}
}

interface SaveHandler<T> extends Serializable {
	void onSave(T entity)
}

interface DeleteHandler<T> extends Serializable {
	void onDelete(T entity)
}