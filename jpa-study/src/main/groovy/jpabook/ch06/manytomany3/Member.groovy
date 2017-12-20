package jpabook.ch06.manytomany3

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

import groovy.transform.EqualsAndHashCode
import groovy.transform.TypeChecked

@TypeChecked
@EqualsAndHashCode
@Entity(name = "jpabook.ch06.manytomany3.Member")
@Table(name = "MEMBER")
class Member {
	@Id
	@Column(name = "MEMBER_ID")
	String id
	
	String username
	
	@OneToMany(mappedBy = "member")
	List<OrderItem> orders = []
}
