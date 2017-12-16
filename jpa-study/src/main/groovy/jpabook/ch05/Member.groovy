package jpabook.ch05

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import groovy.transform.TypeChecked

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table

@TypeChecked
@ToString(includeNames = true, includeFields = true)
@EqualsAndHashCode
@Entity(name = "jpabook.ch05.Member")
@Table(name = "MEMBER")
class Member {
	@Id
	@Column(name = "MEMBER_ID")
	String id

	String username
	
	@ManyToOne
	@JoinColumn(name = "TEAM_ID")
	Team team
	
	def void setTeam(Team team) {
		if (this.team != null)
			(this.team.members as List<Member>).remove(this)

		this.team = team
		(team.members as List<Member>).add(this)
	}
}
