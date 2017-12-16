package jpabook.start

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TypeChecked

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@TypeChecked
@ToString(includeNames = true, includeFields = true)
@EqualsAndHashCode
@Entity(name = "jpabook.start.OrderItem")
@Table(name = "OrderItem")
class OrderItem {
	@Id
	@GeneratedValue
	@Column(name = "ORDER_ITEM_ID")
	Long id
	
	@ManyToOne
	@JoinColumn(name = "ITEM_ID")
	Item item
	
	@ManyToOne
	@JoinColumn(name = "ODER_ID")
	Order order
	
	int orderPrice
	int count
}
