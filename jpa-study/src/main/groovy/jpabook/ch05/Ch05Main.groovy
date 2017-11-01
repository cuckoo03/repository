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
			
			def member1 = new Member(id:"m1", username:"name1")
			member1.team = team1
			em.persist(member1)
			
			def findMember1 = em.find(Member.class, member1.id)
			println "findMember1 team:$findMember1.team"
			
			queryLogicJoin(em)

			tx.commit()			
		} catch (Exception e) {
			e.printStackTrace()
			tx.rollback()
		}
	}
	
	static void queryLogicJoin(EntityManager em) {
		def jpql = "select m from jpabook.ch05.Member m join m.team t where t.id=:teamId"
		
		def resultList = em.createQuery(jpql, Member.class)
				.setParameter("teamId", "t1")
				.getResultList()
		
		println "$resultList"
		resultList.each {
			println "member.username=${it.username}"
		}
	}
}
