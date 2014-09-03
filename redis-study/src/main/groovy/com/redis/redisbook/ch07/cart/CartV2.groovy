package com.redis.redisbook.ch07.cart

import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import org.json.simple.parser.ParseException

import redis.clients.jedis.Jedis
import redis.clients.jedis.Pipeline

import com.redis.redisbook.ch07.JedisHelper

/**
 * 제디스 파이프라인을 이용한 장바구니 클래스 
 * @author cuckoo03
 *
 */
class CartV2 {
	private Jedis jedis
	private JSONObject cartInfo
	private String userNo
	private static final String KEY_CART_LIST = ":cart:product"
	private static final String KEY_CART_PRODUCT = ":cart:productid:"
	private static final String JSON_PRODUCT_LIST = "products"
	private static final int EXPIRE = 60 * 60 * 24 * 3

	public CartV2(JedisHelper helper, String userNo) {
		this.jedis = helper.getConnection()
		this.userNo = userNo
		this.cartInfo = getCartInfo()
	}

	private JSONObject getCartInfo() {
		String productInfo = this.jedis.get(this.userNo + KEY_CART_LIST)
		if (null == productInfo || "".equals(productInfo)) {
			return makeEmptyCart()
		}

		try {
			JSONParser parser = new JSONParser()
			return parser.parse(productInfo)
		} catch (ParseException e) {
			println e
			makeEmptyCart()
		}
	}

	private JSONObject makeEmptyCart() {
		JSONObject cart = new JSONObject()
		cart.put(JSON_PRODUCT_LIST, new JSONArray())
		return cart
	}

	public int flushCart() {
		JSONArray products = this.cartInfo.get(JSON_PRODUCT_LIST)

		Pipeline p = jedis.pipelined()
		products.each {
			p.del(this.userNo + KEY_CART_PRODUCT + it)
		}
		p.set(this.userNo + KEY_CART_LIST, "")
		p.sync()

		return products.size()
	}

	public String addProduct(String productNo, String productName, int quantity) {
		JSONArray products = this.cartInfo.get(JSON_PRODUCT_LIST)
		products.add(productNo)

		this.jedis.set(this.userNo + KEY_CART_LIST, this.cartInfo.toJSONString())

		JSONObject product = new JSONObject()
		product.put("productNo", productNo)
		product.put("productName", productName)
		product.put("quantity", quantity)

		String productKey = this.userNo + KEY_CART_PRODUCT + productNo
		return this.jedis.setex(productKey, EXPIRE, product.toJSONString())
	}

	public int deleteProduct(String[] productNo) {
		JSONArray products = this.cartInfo.get(JSON_PRODUCT_LIST)
		int result = products.size()

		Pipeline p = jedis.pipelined()
		products.each { item ->
			products.remove(item)
			p.del(this.userNo + KEY_CART_PRODUCT + item)
		}
		p.set(this.userNo + KEY_CART_LIST, this.cartInfo.toJSONString())
		p.sync()

		return result
	}

	public JSONArray getProductList() {
		boolean isChanged = false
		JSONArray products = this.cartInfo.get(JSON_PRODUCT_LIST)

		Pipeline p = jedis.pipelined()
		products.each {
			p.get(this.userNo + KEY_CART_PRODUCT + it)
		}
		List<Object> redisResult = p.syncAndReturnAll()

		JSONArray result = new JSONArray()
		redisResult.each {
			if (it == null) {
				isChanged = true
			} else {
				result.add(it)
			}
		}
		if (isChanged) {
			this.jedis.set(this.userNo + KEY_CART_LIST,
					this.cartInfo.toJSONString())
		}
		
		return result
	}
	
	@Deprecated
	public int flushCartDeprecated() {
		Set<String> keys = this.jedis.keys(this.userNo + KEY_CART_PRODUCT + "*")
		
		Pipeline p = jedis.pipelined()
		keys.each { key ->
			p.del(key)
		}
		p.set(this.userNo + KEY_CART_LIST, "")
		p.sync()
		
		return keys.size()
	}
}