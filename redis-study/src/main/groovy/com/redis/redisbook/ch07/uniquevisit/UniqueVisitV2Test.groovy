package com.redis.redisbook.ch07.uniquevisit

import static org.junit.Assert.*

import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import com.redis.redisbook.ch07.JedisHelper

class UniqueVisitV2Test {
	static JedisHelper helper
	private UniqueVisitV2 uniqueVisit
	private static final int TOTAL_USER = 100000000
	
	@BeforeClass
	public static void setUpClass() {
		helper = JedisHelper.getInstance()
	}
	
	@AfterClass
	public static void tearDownClass() {
		helper.destroyPool()
	}
	
	@Before
	public void setUp() {
		uniqueVisit = new UniqueVisitV2(helper)
		assertNotNull(uniqueVisit)
		
		uniqueVisit.visit(1, "20140904")
		uniqueVisit.visit(2, "20140904")
		uniqueVisit.visit(3, "20140904")
		uniqueVisit.visit(TOTAL_USER, "20140904")
	}

	@Test
	public void testUVSum() {
		String[] dateList = ["20140904"]
		assertEquals(new Long(4), uniqueVisit.getUVSum(dateList))
	}
}
