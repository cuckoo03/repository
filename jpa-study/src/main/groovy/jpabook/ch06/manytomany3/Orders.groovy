package jpabook.ch06.manytomany3

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.SequenceGenerator
import javax.persistence.Table

import groovy.transform.EqualsAndHashCode
import groovy.transform.TypeChecked

@TypeChecked
@EqualsAndHashCode
@Entity(name = "jpabook.ch06.manytomany3.Orders")
@Table(name = "ORDERS")
class Orders {
	@Id
	@GeneratedValue
	@Column(name = "ORDER_ID")
	Long id
	
	@ManyToOne
	@JoinColumn(name = "MEMBER_ID")
	Member member
	
	@ManyToOne
	@JoinColumn(name = "PRODUCT_ID")
	Product product

	int orderAmount
}
