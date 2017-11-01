package jpabook.start

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TypeChecked

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id
import javax.persistence.Lob
import javax.persistence.Table
import javax.persistence.Temporal
import javax.persistence.TemporalTypeimport javax.persistence.UniqueConstraint;


@TypeChecked
@ToString(includeNames = true, includeFields = true)
@EqualsAndHashCode
@Entity(name = "jpabook.start.Member")
@Table(name = "MEMBER")
class Member {
	@Id
	@Column(name = "ID")
	String id

	@Column(name = "NAME", nullable = false, length = 10)
	String username

	@Column(name = "AGE")
	Integer age

	@Enumerated(EnumType.STRING)
	RoleType roleType

	@Temporal(TemporalType.TIMESTAMP)
	Date createdDate

	@Temporal(TemporalType.TIMESTAMP)
	Date lastModifiedDate

	@Lob
	String description

	enum RoleType {
		ADMIN, USER
	}
}
