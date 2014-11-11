package org.jboss.netty.example.http.snoop;

import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.http.CookieEncoder;
import org.jboss.netty.handler.codec.http.DefaultHttpRequest;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpVersion;

public class HttpClient {
	/**
	 * 
	 * @param args
	 * @throws URISyntaxException
	 */
	public static void main(String[] args) throws URISyntaxException {
		args = new String[]{"https://127.0.0.1"};
		if (args.length != 1) {
			System.err.println("Usage: " + HttpClient.class.getSimpleName()
					+ " <URL>");
			return;
		}
		URI uri = new URI(args[0]);
		String scheme = uri.getScheme() == null ? "http" : uri.getScheme();
		String host = uri.getHost() == null ? "localhost" : uri.getHost();
		int port = uri.getPort();

		if (-1 == port) {
			if (scheme.equalsIgnoreCase("http")) {
				port = 8080;
			} else if (scheme.equalsIgnoreCase("https")) {
				port = 443;
			}
		}

		if (!scheme.equalsIgnoreCase("http")
				&& !scheme.equalsIgnoreCase("https")) {
			System.err.println("Only HTTP(S) is supported");
			return;
		}

		boolean ssl = scheme.equalsIgnoreCase("https");

		ChannelFactory factory = new NioClientSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool());

		ClientBootstrap bootstrap = new ClientBootstrap(factory);
		bootstrap.setPipelineFactory(new HttpClientPipelineFactory(ssl));

		ChannelFuture future = bootstrap.connect(new InetSocketAddress(host,
				port));
		System.out.println("client connect");
		
		Channel channel = future.awaitUninterruptibly().getChannel();
		if (!future.isSuccess()) {
			future.getCause().printStackTrace();
			bootstrap.releaseExternalResources();
			return;
		}

		HttpRequest request = new DefaultHttpRequest(HttpVersion.HTTP_1_1,
				HttpMethod.GET, uri.toASCIIString());
		request.setHeader(HttpHeaders.Names.HOST, host);
		request.setHeader(HttpHeaders.Names.CONNECTION,
				HttpHeaders.Values.CLOSE);
		request.setHeader(HttpHeaders.Names.ACCEPT_ENCODING,
				HttpHeaders.Values.GZIP);

		CookieEncoder httpCookieEncoder = new CookieEncoder(false);
		httpCookieEncoder.addCookie("my-ccookie", "foo");
		// 두개이상의 쿠키를 추가시에 런타임 오류 발생
		// java.lang.IllegalStateException: encode() can encode only one cookie on server mode: 2 cookies added
//		httpCookieEncoder.addCookie("another-cookie", "bar");
		request.setHeader(HttpHeaders.Names.COOKIE,
				httpCookieEncoder.encode());
		
		channel.write(request);
		
		channel.getCloseFuture().awaitUninterruptibly();
		
		bootstrap.releaseExternalResources();	
	}
}