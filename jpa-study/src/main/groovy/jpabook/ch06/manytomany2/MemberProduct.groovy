package jpabook.ch06.manytomany2

import groovy.transform.EqualsAndHashCode
import groovy.transform.TypeChecked

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.IdClass;
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@TypeChecked
//@EqualsAndHashCode
@Entity(name = "jpabook.ch06.manytomany2.MemberProduct")
@IdClass(MemberProductId.class)
class MemberProduct {
	@Id
	@ManyToOne
	@JoinColumn(name = "MEMBER_ID")
	Member member

	@Id
	@ManyToOne
	@JoinColumn(name = "PRODUCT_ID")
	Product product
	
	int orderAmount
}
