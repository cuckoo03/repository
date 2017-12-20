package jpabook.ch06.manytomany2

import groovy.transform.EqualsAndHashCode;
import groovy.transform.TypeChecked

import javax.persistence.Column
import javax.persistence.Id

@TypeChecked
@EqualsAndHashCode
class MemberProductId implements Serializable {
	String member 
	String product
}
