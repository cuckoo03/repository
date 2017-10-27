package jpabook.start

import groovy.transform.Canonical;
import groovy.transform.EqualsAndHashCode;
import groovy.transform.ToString;
import groovy.transform.TypeChecked

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@TypeChecked
@ToString(includeNames = true, includeFields = true)
@EqualsAndHashCode
@Entity
@Table(name = "MEMBER")
class Member {
	@Id
	@Column(name = "ID")
	private String id

	@Column(name = "NAME")
	private String username

	@Column(name = "AGE")
	private Integer age
}
