package com.elasticsearch.client

import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import com.elasticsearch.client.dao.TapacrossDao

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import groovy.transform.TypeChecked

@TypeChecked
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/application-context.xml")
class GTest {
	@Autowired
	private TapacrossDao dao
	
	/**
	 * 엘라스틱서치에 json 형태로 데이터를 임포트하기 위한 json 데이터 생성 메서드
	 */
	@Test
	def void test() {
		def start = 15000
		def end = 16000
		def tableName = "tb_article_search_media_2012"
		def result = dao.selectArticles(start, end, tableName)
		assert result.size() > 0
		
		result.each {
			def json = new JsonBuilder()
			def map = [:]
			
			def seq = "{\"index\" : {\"_id\":\"$it.seq\"}}"
			println seq

			map["article_id"] = it.articleId
			map["content_id"] = it.contentId
			map["site_id"] = it.siteId
			map["writer_id"] = it.writerId
			map["title"] = it.title
			map["body"] = it.body
			map["rt"] = randomBinaryCount()
			map["reply_id"] = ""
			map["re"] = randomBinaryCount()
			map["address"] = ""
			map["address2"] = ""
			map["create_date"] = it.createDate
			map["site_type"] = it.siteType
			map["via_url"] = ""
			map["url"] = it.url
			map["rt_count"] = randomCount()
			map["follower_count"] = randomCount()
			map["site_name"] = it.siteName
			map["picture"] = it.picture
			map["screen_name"] = it.screenName
			map["site_category"] = it.siteCategory
			map["publisher_type"] = "C"
			map["sentiment"] = randomSentiment()
			map["hashtag"] = it.body
			map["topic"] = it.body
			map["sentiment_topic"] = it.body
			map["tpo_topic"] = it.body
			map["hit_count"] = randomCount()
			map["comment_count"] = randomCount()
			map["like_count"] = randomCount()
			map["dislike_count"] = randomCount()

			json.call(map) 
			println json
		}
	}
	
	def void testRandom() {
		while (true) {
			println randomSentiment()
			sleep(100)
		}
	}
	private int randomSentiment() {
		return new Random().nextInt(3)
	}
	private int randomCount() {
		return new Random().nextInt(1000)
	}
	private int randomBinaryCount() {
		return new Random().nextInt(2)
	}
}
