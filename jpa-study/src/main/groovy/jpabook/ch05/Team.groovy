package jpabook.ch05

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TypeChecked

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.OneToMany;
import javax.persistence.Table

@TypeChecked
@ToString(includeNames = true, includeFields = true)
@EqualsAndHashCode
@Entity(name = "jpabook.ch05.Team")
@Table(name = "TEAM")
class Team {
	@Id
	@Column(name = "TEAM_ID")
	String id

	@Column(name = "NAME")
	String name
	
	@OneToMany(mappedBy = "team")
	List<Member> members = new ArrayList<>() 
}
