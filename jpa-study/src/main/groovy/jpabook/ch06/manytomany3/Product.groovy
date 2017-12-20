package jpabook.ch06.manytomany3

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

import groovy.transform.EqualsAndHashCode
import groovy.transform.TypeChecked

@TypeChecked
@EqualsAndHashCode
@Entity(name = "jpabook.ch06.manytomany3.Product")
@Table(name = "PRODUCT")
class Product {
	@Id
	@Column(name = "PRODUCT_ID")
	String id
	
	String name
}
