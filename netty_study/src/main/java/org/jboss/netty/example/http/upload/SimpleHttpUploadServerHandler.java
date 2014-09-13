package org.jboss.netty.example.http.upload;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.http.Cookie;
import org.jboss.netty.handler.codec.http.CookieDecoder;
import org.jboss.netty.handler.codec.http.CookieEncoder;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpChunk;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.jboss.netty.handler.codec.http.multipart.Attribute;
import org.jboss.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import org.jboss.netty.handler.codec.http.multipart.DiskAttribute;
import org.jboss.netty.handler.codec.http.multipart.DiskFileUpload;
import org.jboss.netty.handler.codec.http.multipart.FileUpload;
import org.jboss.netty.handler.codec.http.multipart.HttpDataFactory;
import org.jboss.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import org.jboss.netty.handler.codec.http.multipart.HttpPostRequestDecoder.EndOfDataDecoderException;
import org.jboss.netty.handler.codec.http.multipart.HttpPostRequestDecoder.IncompatibleDataDecoderException;
import org.jboss.netty.handler.codec.http.multipart.HttpPostRequestDecoder.NotEnoughDataDecoderException;
import org.jboss.netty.handler.codec.http.multipart.InterfaceHttpData;
import org.jboss.netty.handler.codec.http.multipart.HttpPostRequestDecoder.ErrorDataDecoderException;
import org.jboss.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;
import org.jboss.netty.util.CharsetUtil;

public class SimpleHttpUploadServerHandler extends SimpleChannelUpstreamHandler {
	private static final HttpDataFactory factory = new DefaultHttpDataFactory(
			DefaultHttpDataFactory.MINSIZE);
	private HttpPostRequestDecoder decoder;
	private boolean readingChunks;
	private HttpRequest request;
	private StringBuilder responseContent = new StringBuilder();

	static {
		DiskFileUpload.deleteOnExitTemporaryFile = false;
		DiskFileUpload.baseDirectory = "c:/";
		DiskAttribute.deleteOnExitTemporaryFile = false;
		DiskAttribute.baseDirectory = "c:/";
	}

	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) {
		if (null != decoder) {
			decoder.cleanFiles();
		}
	}

	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		if (!readingChunks) {
			// fileupload 이전에 decoder 초기화
			if (null != decoder) {
				decoder.cleanFiles();
				decoder = null;
			}

			HttpRequest request = this.request = (HttpRequest) e.getMessage();

			// protocol 정보 출력
			// request header 정보 출력
			// cookie 정보 출력
			// QueryStringDecoder 정보 출력
			// HttpPostRequestDecoder 정보 출력
			try {
				decoder = new HttpPostRequestDecoder(factory, request);
			} catch (ErrorDataDecoderException e1) {
				e1.printStackTrace();
				writeResponse(e.getChannel());
				return;
			} catch (IncompatibleDataDecoderException e1) {
				// receive data가 완료되지 않았으면 발생하며 종료후 이후의 received를 받음
				System.out.println(e1.getCause().toString());
				writeResponse(e.getChannel());
				return;
			}

			if (request.isChunked()) {
				// chunk version
				readingChunks = true;
			} else {
				// chunk version
				// body에 있는 HttpData 정보 출력
				// channel write하고 종료
				readHttpDataAllReceive(e.getChannel());
				e.getChannel().close();
			}
		} else {
			// new chunk is received
			HttpChunk chunk = (HttpChunk) e.getMessage();
			try {
				decoder.offer(chunk);
			} catch (ErrorDataDecoderException e1) {
				e1.printStackTrace();
				// error 로깅 처리후 client write, Channel close
				e.getChannel().close();
			}

			// reading all InterfaceHttpData from finished transfer
			readHttpDataChunkByChunk(e.getChannel());
			if (chunk.isLast()) {
				writeResponse(e.getChannel());
				readingChunks = false;
			}
		}
	}

	// reading all interfaceHttpData from finished transfer
	private void readHttpDataAllReceive(Channel channel) {
		List<InterfaceHttpData> datas;
		try {
			datas = decoder.getBodyHttpDatas();
		} catch (NotEnoughDataDecoderException e) {
			e.printStackTrace();
			writeResponse(channel);
			Channels.close(channel);
			return;
		}

		for (InterfaceHttpData data : datas) {
			// read http data
		}
		
		// end of content at final end
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		e.getCause().printStackTrace();
		e.getChannel().close();
	}

	// reading request by chunk and getting values from chunk to chunk
	private void readHttpDataChunkByChunk(Channel channel) {
		try {
			while (decoder.hasNext()) {
				InterfaceHttpData data = decoder.next();
				if (null != data) {
					// new value
					readHttpData(data);
				}
			}
		} catch (EndOfDataDecoderException e) {
			// end
			System.out.println("\r\n end of content chunk by chunk:"
					+ e.getMessage());
		}
	}

	private void readHttpData(InterfaceHttpData data) {
		if (data.getHttpDataType() == HttpDataType.Attribute) {
			Attribute attribute = (Attribute) data;
			String value;
			try {
				value = attribute.getValue();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("datatype:" + data.getHttpDataType().name()
					+ "=" + data.toString());
			if (data.getHttpDataType() == HttpDataType.FileUpload) {
				FileUpload fileUpload = (FileUpload) data;
				if (fileUpload.isCompleted()) {
					try {
						fileUpload.getString();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					// File to be continued but should not
				}
			}
		}
	}

	private void writeResponse(Channel channel) {
		ChannelBuffer buf = ChannelBuffers.copiedBuffer(
				responseContent.toString(), CharsetUtil.UTF_8);
		responseContent.setLength(0);

		boolean close = HttpHeaders.Values.CLOSE.equalsIgnoreCase(request
				.getHeader(HttpHeaders.Names.CONNECTION))
				|| request.getProtocolVersion().equals(HttpVersion.HTTP_1_0)
				&& !HttpHeaders.Values.KEEP_ALIVE.equalsIgnoreCase(request
						.getHeader(HttpHeaders.Names.CONNECTION));

		HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1,
				HttpResponseStatus.OK);
		response.setContent(buf);
		response.setHeader(HttpHeaders.Names.CONTENT_TYPE,
				"text/plain;charset=UTF-8");

		if (!close) {
			response.setHeader(HttpHeaders.Names.CONTENT_LENGTH,
					String.valueOf(buf.readableBytes()));
		}

		/*
		 * Set<Cookie> cookies; String value =
		 * request.getHeader(HttpHeaders.Names.COOKIE); if (null == value) {
		 * cookies = Collections.emptySet(); } else { CookieDecoder decoder =
		 * new CookieDecoder(); cookies = decoder.decode(value); } if
		 * (!cookies.isEmpty()) { CookieEncoder cookieEncoder = new
		 * CookieEncoder(true); for (Cookie cookie : cookies) {
		 * cookieEncoder.addCookie(cookie);
		 * response.addHeader(HttpHeaders.Names.SET_COOKIE,
		 * cookieEncoder.encode()); cookieEncoder = new CookieEncoder(true); } }
		 */

		ChannelFuture future = channel.write(response);

		if (close) {
			future.addListener(ChannelFutureListener.CLOSE);
		}
	}
}