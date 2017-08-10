package com.vseminar.data

import groovy.transform.Trait;
import groovy.transform.TypeChecked;

@TypeChecked
@Trait
class VSeminarData<T> {
	abstract T findOne(long id)
	abstract List<T> findAll()
	abstract int count()
	abstract T save(T entity)
	abstract void delete(long id)
}
