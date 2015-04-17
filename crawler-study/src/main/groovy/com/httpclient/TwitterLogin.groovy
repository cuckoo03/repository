package com.httpclient

import groovy.transform.TypeChecked

import org.apache.http.HeaderElement
import org.apache.http.HeaderElementIterator
import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.message.BasicHeaderElementIterator
import org.apache.http.message.BasicNameValuePair
import org.apache.http.util.EntityUtils

@TypeChecked
class TwitterLogin {
	static main(args) {
		String uri = "https://twitter.com/session?phx=1"
		HttpClient client = new DefaultHttpClient()
		HttpPost post = new HttpPost(uri)
		def nameValuePairs = new ArrayList(4)
		nameValuePairs.add(new BasicNameValuePair("session[username_or_email]", "cuckoo03@gmail.com"))
		nameValuePairs.add(new BasicNameValuePair("session[password]", "wolfen11"))
		post.setEntity(new UrlEncodedFormEntity(nameValuePairs))

		HttpResponse response = client.execute(post)
		HeaderElementIterator iter = new BasicHeaderElementIterator(
				response.headerIterator("Set-Cookie"))
		String cookieString = ""
		while (iter.hasNext()) {
			HeaderElement element = iter.nextElement()
			println element.getName() + "=" + element.getValue()
			cookieString += element.getName() + "=" + element.getValue() + ";"
		}
		println "cookieString:$cookieString"
		println "------------------------"
		HttpClient clientResult = new DefaultHttpClient()
		HttpPost postResult = new HttpPost("http://twitter.com")
		postResult.setHeader("Cookie", cookieString)
		HttpResponse responseResult = clientResult.execute(postResult)
		HttpEntity entity = responseResult.getEntity()
		println EntityUtils.toString(entity)
	}
}