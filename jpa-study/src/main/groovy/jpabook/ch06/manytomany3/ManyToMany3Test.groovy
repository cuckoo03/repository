package jpabook.ch06.manytomany3

import groovy.transform.TypeChecked

import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory

import org.springframework.context.support.GenericXmlApplicationContext

@TypeChecked
class ManyToMany3Test {
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
		def member1 = new Member()
		member1.id = "member1"
		member1.username = "m1"
		em.persist(member1)
		
		def productA = new Product()
		productA.id = "productA"
		productA.name = "p1"
		em.persist(productA)
		
		def orderItem = new OrderItem()
		orderItem.member = member1
		orderItem.product = productA
		orderItem.orderAmount = 2
		
		em.persist(orderItem)
	}
	
	static void find(EntityManager em) {
		def order = em.find(OrderItem.class, 1L)
		def member = order.member
		def product = order.product
		
		println "mmeber=$member.username"
		println "product=$product.name"
		println "orderAmount=$order.orderAmount"
	}
}
