package jpabook.start

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TypeChecked

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table
import javax.persistence.Temporal
import javax.persistence.TemporalType

@TypeChecked
@ToString(includeNames = true, includeFields = true)
@EqualsAndHashCode
@Entity(name = "jpabook.start.Order")
@Table(name = "Order")
class Order {
	@Id
	@GeneratedValue
	@Column(name = "ORDER_ID")
	long id
	
	@ManyToOne
	@JoinColumn(name = "MEMBER_ID")
	Member member
	
	@OneToMany(mappedBy = "order")
	List<OrderItem> orderItems = []
	
	@Temporal(TemporalType.TIMESTAMP)
	Date orderDate
	

	@Enumerated(EnumType.STRING)
	OrderStatus status
	
	void setMember(Member member) {
		if (this.member != null)
			(this.member.orders as List).remove(this)
		
		this.member = member
		(member.orders as List).add(this)
	}
	
	void addOrderItem(OrderItem orderItem) {
		orderItems += orderItem
		orderItem.order = this
	}

}
