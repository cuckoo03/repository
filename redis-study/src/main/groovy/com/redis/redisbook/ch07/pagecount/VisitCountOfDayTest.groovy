package com.redis.redisbook.ch07.pagecount
import static org.junit.Assert.*

import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test

import com.redis.redisbook.ch07.JedisHelper

class VisitCountOfDayTest {
	static JedisHelper helper
	
	@BeforeClass
	public static void setUp() {
		helper = JedisHelper.getInstance()
	}
	
	@AfterClass
	public static void tearDown() {
		helper.destroyPool()
	}
	
	@Test
	public void testAddVisit() {
		VisitCount visitCount = new VisitCount(helper)
		assertTrue(visitCount.addVisit("52") > 0)
		assertTrue(visitCount.addVisit("180") > 0)
		assertTrue(visitCount.addVisit("554") > 0)
		
		VisitCountOfDay visitCountOfDay = new VisitCountOfDay(helper) 
		assertTrue(visitCountOfDay.addVisit("52") > 0)
		assertTrue(visitCountOfDay.addVisit("180") > 0)
		assertTrue(visitCountOfDay.addVisit("554") > 0)
	}
	
	@Test
	public void testGetVisitCountByDate() {
		String[] dateList = ["20140512", "20140513", "20140514", "20140901"]
		VisitCountOfDay visitCountOfDay = new VisitCountOfDay(helper)
		List<String> result = visitCountOfDay.getVisitCountByDate("52", dateList)
		assertNotNull(result)
		assertTrue(result.size() == 4)
	}
	
}
