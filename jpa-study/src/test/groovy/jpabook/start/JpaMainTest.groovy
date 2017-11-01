package jpabook.start

import groovy.transform.TypeChecked

import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

import jpabook.start2.Member2;

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.transaction.TransactionConfiguration

/**
 * em.persist실행시 인서트 안됨
 * @author admin
 *
 */
@TypeChecked
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/application-context.xml")
class JpaMainTest {
	@PersistenceContext
	private EntityManager em
	
	@Test
	void test() {
		def id = "id1"
		def member = new Member2()
		member.id = id
		member.username = "name1"
		member.age = 2

		em.persist(member)

		member.age = 20
		
		def findMember = em.find(Member2.class, id)
		System.out.println("findMember:" + findMember)
		println "member equals:${member == findMember}"
	}
}
