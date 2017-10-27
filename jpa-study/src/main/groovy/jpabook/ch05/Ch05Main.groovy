package jpabook.ch05

import groovy.transform.TypeChecked

import javax.persistence.EntityManagerFactory

import org.springframework.context.support.GenericXmlApplicationContext

@TypeChecked
class Ch05Main {
	static main(args) {
		GenericXmlApplicationContext context = new GenericXmlApplicationContext(
				"classpath:spring/application-context.xml")

		def emf = (EntityManagerFactory) context.getBean("entityManagerFactory")
		def em = emf.createEntityManager()
		
		def member1 = new Member(id:"m1", username:"name1")
		def member2 = new Member(id:"m2", username:"name2")
		def team1 = new Team(id:"t1", name:"name1")
		
		member1.team = team1
		member2.team = team1
		
	}
}
