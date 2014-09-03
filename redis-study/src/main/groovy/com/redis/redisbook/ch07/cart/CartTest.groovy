package com.redis.redisbook.ch07.cart

import static org.junit.Assert.*

import org.json.simple.JSONArray;
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

import com.redis.redisbook.ch07.JedisHelper

class CartTest {
	private static final String TESTUSER = "12521"
	static JedisHelper helper
	private Cart cart

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
		this.cart = new Cart(helper, TESTUSER)
		assertNotNull(this.cart)
	}

	@Test
	public void testAddProduct() {
		assertEquals("OK", this.cart.addProduct("151", "원두커피", 1))
		assertEquals("OK", this.cart.addProduct("156", "캔커피", 5))
	}

	@Test
	public void testGetProductList() {
		JSONArray products = this.cart.getProductList()
		assertNotNull(products)
		assertEquals(2, products.size())
	}

	@Test
	public void testDeleteProduct() {
		String[] products = {"151"}
		int result = this.cart.deleteProduct(products)
		assertEquals(1, result)
	}
	
	@Test
	public void testFlushCart() {
		assertTrue(this.cart.flushCart() > -1)
		assertTrue(this.cart.flushCartDeprecated() > -1)
	}
}
