package com.redis.redisbook.ch07.pagecount

import static junit.framework.Assert.*

import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test

import redis.clients.jedis.Jedis

import com.redis.redisbook.ch07.JedisHelper

/**
 * 이벤트 방문 횟수 저장을 위한 테스트 케이스
 * @author cuckoo03
 *
 */
class VisitCountTest {
	static JedisHelper helper
	@BeforeClass
	public static void setUp() {
		helper = JedisHelper.getInstance()
		
		Jedis jedis = helper.getConnection()
		VisitCount visit = new VisitCount(helper)
		assertTrue(visit.deleteVisitCount("52") > 0)
		assertTrue(visit.deleteVisitCount("180") > 0)
		assertTrue(visit.deleteVisitCount("554") > 0)
		assertTrue(visit.deleteVisitTotal() > 0)
	}
	
	@AfterClass
	public static void tearDown() {
		helper.destroyPool()
	}
	
	@Test
	public void testAddVisit() {
		VisitCount visitCount = new VisitCount(helper)
		assertNotNull(visitCount)
		
		assertTrue(visitCount.addVisit("52") > 0)
		assertTrue(visitCount.addVisit("180") > 0)
		assertTrue(visitCount.addVisit("554") > 0)
	}
	
	@Test
	public void testGetVisit() {
		VisitCount visitCount = new VisitCount(helper)
		assertNotNull(visitCount)
		
		List<String> result = visitCount.getVisitCount("52", "180", "554")
		println result
		assertTrue(result.size() == 3)
		
		long sum = 0
		result.each {
			println it instanceof String
			sum = sum + Long.parseLong(it)
		}
		
		String totalCount = visitCount.getVisitTotalCount()
		println "totalCount:$totalCount"
		
		assertEquals(String.valueOf(sum), totalCount)
	}
	
}
