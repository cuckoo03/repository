package com.redis.redisbook.ch07.cart

import org.json.simple.JSONArray
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import org.json.simple.parser.ParseException

import redis.clients.jedis.Jedis

import com.redis.redisbook.ch07.JedisHelper

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
}