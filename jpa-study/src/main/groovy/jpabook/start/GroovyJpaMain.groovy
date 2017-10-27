package jpabook.start

import groovy.transform.TypeChecked

import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory

import org.springframework.context.support.GenericXmlApplicationContext

@TypeChecked
class GroovyJpaMain {
	static main(args) {
		GenericXmlApplicationContext context = new GenericXmlApplicationContext(
				"classpath:spring/application-context.xml")

		def emf = (EntityManagerFactory) context.getBean("entityManagerFactory")
		def em = emf.createEntityManager()
		def tx = em.getTransaction()
		tx.begin()


		def id = "id1"
		def member = new Member()
		member.id = id
		member.username = "name1"
		member.age = 2

		em.persist(member)

		member.age = 20

		def findMember = em.find(Member.class, id)
		System.out.println("findMember:" + findMember)
		println "insert member equals:${member == findMember}"

		//		em.detach(member)
		//		println "remove member. contains:${em.contains(member)}"

		tx.commit()
		em.close()
	}
}
