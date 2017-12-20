package jpabook.ch06.manytomany

import groovy.transform.EqualsAndHashCode
import groovy.transform.TypeChecked

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.Table

@TypeChecked
@EqualsAndHashCode
@Entity(name = "jpabook.ch06.manytomany.Member")
@Table(name = "MEMBER")
class Member {
	@Id
	@Column(name = "MEMBER_ID")
	String id

	String username

	@ManyToMany
	@JoinTable(name = "MEMBER_PRODUCT",
	joinColumns = @JoinColumn(name = "MEMBER_ID"),
	inverseJoinColumns = @JoinColumn(name = "PRODUCT_ID"))
	List<Product> products = []
}
