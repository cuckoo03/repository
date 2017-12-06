package jpabook.ch05

import groovy.transform.TypeChecked

import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory

import org.springframework.context.support.GenericXmlApplicationContext

@TypeChecked
class Ch05Main {
	static main(args) {
		GenericXmlApplicationContext context = new GenericXmlApplicationContext(
				"classpath:spring/application-context.xml")

		def emf = (EntityManagerFactory) context.getBean("entityManagerFactory")
		def em = emf.createEntityManager()
		def tx = em.getTransaction()
		tx.begin()

		try {
			def team1 = new Team(id:"t1", name:"team1")
			em.persist(team1)

			def findTeam1 = em.find(Team.class, "t1")
			println "findTeam1:$findTeam1"

			Member member1 = new Member(id:"m1", username:"user1") 
			member1.team = team1
			em.persist(member1)

			def findMember1 = em.find(Member.class, member1.id)
			println "findMember1 $findMember1"
			println "findMember1 team:$findMember1.team"

			def team2 = new Team(id:"t2", name:"team2")
			em.persist(team2)
			
			//change team
//			findMember1.team = team2
			
			//remove team
//			findMember1.team = null
//			em.remove(team2)

//			queryLogicJoin(em)
			
			biDirection(em)
			tx.commit()
		} catch (Exception e) {
			e.printStackTrace()
			tx.rollback()
		}
	}

	static void biDirection(EntityManager em) {
		def team = em.find(Team.class, "t1")	
		def members = team.members
		
		members.each { Member it -> println "member.username=$it.username" }
	}

	static void queryLogicJoin(EntityManager em) {
		def jpql = "select m from jpabook.ch05.Member m join m.team t where 1=1 and t.name=:teamName"

		def resultList = em.createQuery(jpql, Member.class)
				.setParameter("teamName", "team1")
				.getResultList()

		println "query result:resultList"
		resultList.each { Member it -> println "member.username=$it.username" }
	}
}
