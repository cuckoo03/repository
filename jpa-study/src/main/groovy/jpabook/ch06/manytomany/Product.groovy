package jpabook.ch06.manytomany

import groovy.transform.EqualsAndHashCode
import groovy.transform.TypeChecked

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@TypeChecked
@EqualsAndHashCode
@Entity(name = "jpabook.ch06.manytomany.Product")
@Table(name = "PRODUCT")
class Product {
	@Id
	@Column(name = "PRODUCT_ID")
	String id
	
	String name
}