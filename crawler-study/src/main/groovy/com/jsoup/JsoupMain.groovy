package com.jsoup

import groovy.transform.TypeChecked

import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.HttpResponseException
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.BasicResponseHandler
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.protocol.BasicHttpContext
import org.apache.http.protocol.HttpContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements


/**
 * 파싱예제
 * @author cuckoo03
 *
 */
@TypeChecked
class JsoupMain {
	static main(args) {
		HttpClient httpClient = new DefaultHttpClient()
		HttpGet httpget = new HttpGet("http://en.wikipedia.org/")

		HttpContext context = new BasicHttpContext()
//		context.setAttribute(CoreProtocolPNames.USER_AGENT, "Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.2; WOW64; Trident/6.0)")

		String res = ""
		httpClient.execute(httpget, new BasicResponseHandler(){
					@Override
					public String handleResponse(HttpResponse response) throws
					HttpResponseException,IOException {
						res = new String(super.handleResponse(response))
					}
				}, context)
		
		Document doc = Jsoup.parse(res)
		println doc.getElementsByTag("title")
		Elements newHeadlines = doc.select("#mp-itn b a")
		println newHeadlines
	}
}
