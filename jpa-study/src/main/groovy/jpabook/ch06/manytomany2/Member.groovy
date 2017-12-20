package jpabook.ch06.manytomany2

import groovy.transform.EqualsAndHashCode
import groovy.transform.TypeChecked

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToMany;
import javax.persistence.Table

@TypeChecked
//@EqualsAndHashCode
@Entity(name = "jpabook.ch06.manytomany2.Member")
@Table(name = "MEMBER")
class Member {
	@Id
	@Column(name = "MEMBER_ID")
	String id
	
	@OneToMany(mappedBy = "member")
	List<MemberProduct> memberProducts = []
}
