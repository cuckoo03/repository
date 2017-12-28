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
import javax.persistence.OneToOne
import javax.persistence.Table

@TypeChecked
@ToString(includeNames = true, includeFields = true)
@EqualsAndHashCode
@Entity(name = "jpabook.start.Delivery")
@Table(name = "DELIVERY")
class Delivery {
	@Id
	@GeneratedValue
	@Column(name = "DELIVERY_ID")
	long id

	@OneToOne(mappedBy = "delivery")
	Orders order

	String city
	String street
	String zipcode
	
	@Enumerated(EnumType.STRING)
	DeliveryStatus status
}
