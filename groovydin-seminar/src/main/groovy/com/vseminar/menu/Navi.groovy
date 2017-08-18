package com.vseminar.menu

import groovy.transform.Canonical
import groovy.transform.ToString
import groovy.transform.TypeChecked

import com.vaadin.navigator.View
import com.vaadin.server.Resource
import com.vseminar.data.model.RoleType

@TypeChecked
@Canonical
@ToString(includeFields = true, includeNames = true)
class Navi {
	private String fragment
	private String viewName
	private Class<? extends View> viewClass
	private Resource icon
	private RoleType roleType
}
