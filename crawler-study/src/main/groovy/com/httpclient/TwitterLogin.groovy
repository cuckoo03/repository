package com.httpclient

import groovy.transform.TypeChecked

import org.apache.http.HeaderElement
import org.apache.http.HeaderElementIterator
import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.message.BasicHeaderElementIterator
import org.apache.http.message.BasicNameValuePair
import org.apache.http.util.EntityUtils

@TypeChecked
class TwitterLogin {
	static main(args) {
		String uri = "https://twitter.com/search?q=하둡"
		HttpClient client = new DefaultHttpClient()
		HttpGet post = new HttpGet(uri)

		HttpResponse response = client.execute(post)
		HttpEntity entity = response.getEntity()
		System.out.println("----------------------------------------");
		System.out.println(response.getStatusLine());
		System.out.println("----------------------------------------");
		
		try {
			byte[] buffer = new byte[1024];
			if (entity != null) {
				InputStream inputStream = entity.getContent();
				try {
					int bytesRead = 0;
					BufferedInputStream bis = new BufferedInputStream(inputStream);
					while ((bytesRead = bis.read(buffer)) != -1) {
						String chunk = new String(buffer, 0, bytesRead);
						System.out.println(chunk);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						inputStream.close();
					} catch (Exception ignore) {}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			client.getConnectionManager().shutdown();
		}
	}
}