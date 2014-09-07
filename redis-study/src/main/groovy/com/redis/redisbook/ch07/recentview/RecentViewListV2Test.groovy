package com.redis.redisbook.ch07.recentview

import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import static org.junit.Assert.*

import com.redis.redisbook.ch07.JedisHelper

class RecentViewListV2Test {
	static JedisHelper helper
	private RecentViewListV2 viewList
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
		this.viewList = new RecentViewListV2(helper, TEST_USER)
		assertNotNull(this.viewList)
		this.listMaxSize = viewList.getListMaxSize()
	}
	@Test
	public void testAdd() {
		1.upto(50) {
			this.viewList.add(String.valueOf(it))
		}
	}
	@Test
	public void checkMaxSize() {
		Set set = this.viewList.getRecentViewList()
		int storedSize = set.size()
		assertEquals(this.listMaxSize, storedSize)
	}
	@Test
	public void checkRecentSize() {
		int checkSize = 4
		Set set = this.viewList.getRecentViewList(checkSize)
		int redisSize = set.size()
		
		assertEquals(redisSize, checkSize)
	}
	@Test
	public void checkProductNo() {
		this.viewList.add("48")
		Set set = this.viewList.getRecentViewList()
		assertEquals(set.size(), this.listMaxSize)
		Set<String> itemList = this.viewList.getRecentViewList(5)
		
		itemList.each {
			println it
		}
		String[] list = itemList.toArray(new String[0])
		assertEquals("48", list[0])
	}
}
