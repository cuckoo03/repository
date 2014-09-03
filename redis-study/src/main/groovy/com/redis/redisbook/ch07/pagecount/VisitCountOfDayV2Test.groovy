package com.redis.redisbook.ch07.pagecount

import static org.junit.Assert.*

import java.text.SimpleDateFormat

import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test

import com.redis.redisbook.ch07.JedisHelper

/**
 * 날짜별 이벤트 페이지 방문 횟수 저장 테스트 케이스
 * @author cuckoo03
 *
 */
class VisitCountOfDayV2Test {
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

		VisitCountOfDayV2 visitCountOfDay = new VisitCountOfDayV2(helper)
		assertTrue(visitCountOfDay.addVisit("52") > 0)
		assertTrue(visitCountOfDay.addVisit("180") > 0)
		assertTrue(visitCountOfDay.addVisit("554") > 0)
	}

	@Test
	public void testGetVisitCountByDate() {
		String today = new SimpleDateFormat("yyyyMMdd").format(new Date())
		VisitCountOfDayV2 visitCountOfDay = new VisitCountOfDayV2(helper)

		SortedMap<String, String> visitCount =
				visitCountOfDay.getVisitCountByDaily("554")

		assertTrue(visitCount.size() > 0)
		assertNotNull(visitCount)
		assertNotNull(visitCount.firstKey())
		assertNotNull(visitCount.lastKey())

		println "visitCount:$visitCount"

		SortedMap<String, String> totalVisit =
				visitCountOfDay.getVisitCountByDailyTotal()

		assertTrue(totalVisit.size() > 0)
		assertNotNull(totalVisit)
		assertNotNull(totalVisit.firstKey())
		assertNotNull(totalVisit.lastKey())

		println "totalVisit:$totalVisit"
	}
}
