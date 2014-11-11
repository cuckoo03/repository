package org.jboss.netty.example.http.snoop;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.handler.codec.http.Cookie;
import org.jboss.netty.handler.codec.http.CookieDecoder;
import org.jboss.netty.handler.codec.http.CookieEncoder;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpChunk;
import org.jboss.netty.handler.codec.http.HttpChunkTrailer;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.jboss.netty.handler.codec.http.QueryStringDecoder;
import org.jboss.netty.util.CharsetUtil;

public class HttpServerRequestHandler extends SimpleChannelHandler {
	private boolean readingChunks;
	private HttpRequest request;
	private final StringBuilder buf = new StringBuilder();

	@SuppressWarnings("deprecation")
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		if (!readingChunks) {
			HttpRequest request = this.request = (HttpRequest) e.getMessage();
			// netty 3.2.3에서 추가된 API
			if (HttpHeaders.is100ContinueExpected(request)) {
				send100Continue(e);
			}
			 
			buf.setLength(0);
			buf.append("WELCOME TO THE WILD WILD WEB SERVER\r\n");
			buf.append("===================================\r\n");

			buf.append("VERSION: " + request.getProtocolVersion() + "\r\n");
			buf.append("HOSTNAME: " + HttpHeaders.getHost(request, "unknown")
					+ "\r\n");
			buf.append("REQUEST_URI: " + request.getUri() + "\r\n\r\n");

			for (Map.Entry<String, String> h : request.getHeaders()) {
				buf.append("Header : " + h.getKey() + "=" + h.getValue()
						+ "\r\n");
			}
			buf.append("\r\n");

			QueryStringDecoder queryStringDecoder = new QueryStringDecoder(
					request.getUri());
			Map<String, List<String>> params = queryStringDecoder
					.getParameters();
			if (!params.isEmpty()) {
				for (Map.Entry<String, List<String>> p : params.entrySet()) {
					String key = p.getKey();
					List<String> vals = p.getValue();
					for (String val : vals) {
						buf.append("Param: " + key + "=" + val + "\r\n");
					}
				}
				buf.append("\r\n");
			}

			System.out.println("isChunked:" + request.isChunked());
			if (request.isChunked()) {
				readingChunks = true;
			} else {
				ChannelBuffer content = request.getContent();
				buf.append("content buffer:" + content.toString());
				if (content.readable()) {
					buf.append("content:"
							+ content.toString(CharsetUtil.UTF_8 + "\r\n"));
				}
				writeResponse(e);
			}
		} else {
			HttpChunk chunk = (HttpChunk) e.getMessage();
			if (chunk.isLast()) {
				readingChunks = false;
				buf.append("End of content \r\n");

				HttpChunkTrailer trailer = (HttpChunkTrailer) chunk;
				if (!trailer.getHeaderNames().isEmpty()) {
					buf.append("\r\n");
					for (String name : trailer.getHeaderNames()) {
						for (String value : trailer.getHeaders(name)) {
							buf.append("Trailing header: " + name + "=" + value
									+ "\r\n");
						}
					}
					buf.append("\r\n");
				}

				writeResponse(e);
			} else {
				buf.append("chunk: "
						+ chunk.getContent().toString(CharsetUtil.UTF_8));
			}
		}
	}

	private void writeResponse(MessageEvent e) {
		boolean keepAlilve = HttpHeaders.isKeepAlive(request);

		HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1,
				HttpResponseStatus.OK);
		response.setContent(ChannelBuffers.copiedBuffer(buf.toString(),
				CharsetUtil.UTF_8));
		response.setHeader(HttpHeaders.Names.CONTENT_TYPE,
				"text/plain; charset=UTF-8");

		if (keepAlilve) {
			response.setHeader(HttpHeaders.Names.CONTENT_LENGTH, response
					.getContent().readableBytes());
		}

		//Encode the cookie
		String cookieString = request.getHeader(HttpHeaders.Names.COOKIE);
		if (null != cookieString) {
			CookieDecoder cookieDecoder = new CookieDecoder();
			Set<Cookie> cookies = cookieDecoder.decode(cookieString);
			if (!cookies.isEmpty()) {
				// Reset the cookies if necessary
				CookieEncoder cookieEncoder = new CookieEncoder(true);
				for (Cookie cookie : cookies) {
					cookieEncoder.addCookie(cookie);
				}
				response.addHeader(HttpHeaders.Names.SET_COOKIE,
						cookieEncoder.encode());
			}
		} else {
			CookieEncoder cookieEncoder = new CookieEncoder(true);
			cookieEncoder.addCookie("key1", "value1");
			response.addHeader(HttpHeaders.Names.SET_COOKIE,
					cookieEncoder.encode());
			cookieEncoder.addCookie("key2", "value2");
			response.addHeader(HttpHeaders.Names.SET_COOKIE,
					cookieEncoder.encode());
		}

		ChannelFuture future = e.getChannel().write(response);

		if (!keepAlilve) {
			future.addListener(ChannelFutureListener.CLOSE);
		}
	}

	private void send100Continue(MessageEvent e) {
		HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1,
				HttpResponseStatus.CONTINUE);
		e.getChannel().write(response);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		e.getCause().printStackTrace();
		e.getChannel().close();
	}
}
