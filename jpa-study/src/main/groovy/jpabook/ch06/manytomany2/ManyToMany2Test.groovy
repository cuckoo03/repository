package jpabook.ch06.manytomany2

import groovy.transform.TypeChecked

import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory

import org.springframework.context.support.GenericXmlApplicationContext

@TypeChecked
class ManyToMany2Test {
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
		em.persist(member1)
		
		def productA = new Product()
		productA.id = "productA"
		em.persist(productA)
		
		def memberProduct = new MemberProduct()
		memberProduct.member = member1
		memberProduct.product = productA
		memberProduct.orderAmount = 2
		em.persist(memberProduct)
	}
	
	static void find(EntityManager em) {
		def mpId = new MemberProductId()
		mpId.member = "member1"
		mpId.product = "productA"
		
		def memberProduct = em.find(MemberProduct.class, mpId)
		def member = memberProduct.member
		def product = memberProduct.product
		println "member:$member"
		println "product:$product"
		println "orderAmount:$memberProduct.orderAmount"
	}
}
