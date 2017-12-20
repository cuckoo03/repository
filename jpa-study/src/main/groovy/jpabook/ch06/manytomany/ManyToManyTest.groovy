package jpabook.ch06.manytomany

import groovy.transform.TypeChecked

import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory

import org.springframework.context.support.GenericXmlApplicationContext

@TypeChecked
class ManyToManyTest {
	static main(args) {
		GenericXmlApplicationContext context = new GenericXmlApplicationContext(
				"classpath:spring/application-context.xml")

		def emf = (EntityManagerFactory) context.getBean("entityManagerFactory")
		def em = emf.createEntityManager()
		def tx = em.getTransaction()
		tx.begin()

		try {
			save(em)
			find(em)
			tx.commit()
		} catch (Exception e) {
			e.printStackTrace()
			tx.rollback()
		}
	}
	
	static void save(EntityManager em) {
		def productA = new Product()
		productA.id = "productA"
		productA.name = "a"
		em.persist(productA)
		
		def member1 = new Member()
		member1.id = "member1"
		member1.username = "m1"
		(member1.products as List).add(productA)
		em.persist(member1)
	}
	static void find(EntityManager em) {
		def member = em.find(Member.class, "member1")
		def products = member.products
		products.each { Product it ->
			println "product.name=$it.name"
		}
	}
}
