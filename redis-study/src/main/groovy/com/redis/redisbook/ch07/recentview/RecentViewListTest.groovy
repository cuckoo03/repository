package com.redis.redisbook.ch07.recentview

import static org.junit.Assert.*

import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Ignore
import org.junit.Test

import com.redis.redisbook.ch07.JedisHelper

/**
 * 
 * @author cuckoo03
 *
 */
class RecentViewListTest {
	static JedisHelper helper
	private RecentViewList viewList
	private static final String TEST_USER = "123"
	private int listMaxSize

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
		this.viewList = new RecentViewList(helper, TEST_USER)
		assertNotNull(this.viewList)
		this.listMaxSize = this.viewList.getListMaxSize()
	}

	@Test
	public void testAdd() {
		1.upto(50) {
			this.viewList.add(String.valueOf(it))
		}
	}

	@Test
	public void checkMaxSize() {
		List list = this.viewList.getRecentViewList()
		int storedSize = list.size()
		assertEquals(this.listMaxSize, storedSize)
	}

	@Test
	public void checkRecentSize() {
		int checkSize = 4
		List list = this.viewList.getRecentViewList(checkSize)
		int redisSize = list.size()
		assertEquals(redisSize, checkSize)
	}
	
	@Test
	public void checkProductNo() {
		this.viewList.add("50")
		List list = this.viewList.getRecentViewList()
		assertEquals(list.size(), this.listMaxSize)
		
		List<String> itemList = this.viewList.getRecentViewList(5)
		assertEquals("50", itemList.get(0))
		itemList.each {
			println it
		}
	}
}
