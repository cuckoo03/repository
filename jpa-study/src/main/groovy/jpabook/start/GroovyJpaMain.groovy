package jpabook.start

import java.util.List;

import groovy.transform.TypeChecked

import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory

import jpabook.start2.Member2;

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

		logic1(em)

		tx.commit()
		em.close()
	}
	static void logic1(EntityManager em) {
		def id = 1L
		def member = new Member()
		member.id = id

		em.persist(member)

		def findMember = em.find(Member.class, id)
		System.out.println("findMember:" + findMember)
		println "insert member equals:${member == findMember}"

		def members = em.createQuery("select m from jpabook.start.Member m", Member.class).getResultList();
		System.out.println("members.size=" + members.size());
	}
}
