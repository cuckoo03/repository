package jpabook.ch06.onetoone

import groovy.transform.TypeChecked

import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory

import org.springframework.context.support.GenericXmlApplicationContext

@TypeChecked
class ch06Main {
	static main(args) {
		GenericXmlApplicationContext context = new GenericXmlApplicationContext(
				"classpath:spring/application-context.xml")

		def emf = (EntityManagerFactory) context.getBean("entityManagerFactory")
		def em = emf.createEntityManager()
		def tx = em.getTransaction()
		tx.begin()

		try {
			test1(em)
			tx.commit()
		} catch (Exception e) {
			e.printStackTrace()
			tx.rollback()
		}
	}
	
	static void test1(EntityManager em) {
		def locker = new Locker(id:11, name:"L1")
		em.persist(locker)

		def member = new Member(id:1, username:"M1")
		member.locker = locker
		em.persist(member)
		
		def findMember = em.find(Member.class, member.id)
		println "findMember:$findMember"
		println "findMember locker:$findMember.locker"
	}
}
