package com.redis.redisbook.ch07.uniquevisit

import static org.junit.Assert.*

import java.text.SimpleDateFormat;

import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Ignore;
import org.junit.Test

import com.redis.redisbook.ch07.JedisHelper

class UniqueVisitTest {
	static JedisHelper helper
	private UniqueVisit uniqueVisit
	private static final int VISIT_COUNT = 1000
	private static final int TOTAL_USER = 10000000
	private static final String TEST_DATE = "19500101"
	static Random rand = new Random()

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
		uniqueVisit = new UniqueVisit(helper)
		assertNotNull(uniqueVisit)
	}

	@Test
	public void testRandomPV() {
		int pv = uniqueVisit.getPVCount(getToday())
		VISIT_COUNT.times {
			uniqueVisit.visit(rand.nextInt(TOTAL_USER))
		}

		assertEquals(pv + VISIT_COUNT, uniqueVisit.getPVCount(getToday()))
	}

	@Test
	public void testInvalidPV() {
		assertEquals(0, uniqueVisit.getPVCount(TEST_DATE))
		assertEquals(new Long(0), uniqueVisit.getUVCount(TEST_DATE))
	}

	@Test
	public void testPV() {
		int result = uniqueVisit.getPVCount(getToday())
		uniqueVisit.visit(1)

		assertEquals(result + 1, uniqueVisit.getPVCount(getToday()))
	}

	@Test
	public void testUV() {
		uniqueVisit.visit(1)
		Long result = uniqueVisit.getUVCount(getToday())
		uniqueVisit.visit(1)

		assertEquals(result, uniqueVisit.getUVCount(getToday() ))
	}

	private String getToday() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd")
		return sdf.format(new Date())
	}
}
