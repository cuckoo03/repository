package jpabook.ch06.onetoone

import groovy.transform.EqualsAndHashCode
import groovy.transform.TypeChecked

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.Table

@TypeChecked
//@ToString(includeNames = true, includeFields = true)
@EqualsAndHashCode
@Entity(name = "jpabook.ch06.onetoone.Locker")
@Table(name = "LOCKER")
class Locker {
	@Id
	@Column(name = "LOCKER_ID")
	long id
	
	String name
	
	@OneToOne
	@JoinColumn(name = "MEMBER_ID")
	Member member
}
