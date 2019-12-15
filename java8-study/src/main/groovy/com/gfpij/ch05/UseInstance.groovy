package com.gfpij.ch05

import groovy.transform.TypeChecked

@TypeChecked
@FunctionalInterface
interface UseInstance<T, X extends Throwable> {
	void accept(T instance) throws X
}
