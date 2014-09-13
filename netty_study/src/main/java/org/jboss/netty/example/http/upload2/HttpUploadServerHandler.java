package org.jboss.netty.example.http.upload2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.http.Cookie;
import org.jboss.netty.handler.codec.http.CookieDecoder;
import org.jboss.netty.handler.codec.http.CookieEncoder;
import org.jboss.netty.handler.codec.http.DefaultHttpRequest;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpChunk;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.jboss.netty.handler.codec.http.QueryStringDecoder;
import org.jboss.netty.handler.codec.http.multipart.Attribute;
import org.jboss.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import org.jboss.netty.handler.codec.http.multipart.DiskAttribute;
import org.jboss.netty.handler.codec.http.multipart.DiskFileUpload;
import org.jboss.netty.handler.codec.http.multipart.FileUpload;
import org.jboss.netty.handler.codec.http.multipart.HttpDataFactory;
import org.jboss.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import org.jboss.netty.handler.codec.http.multipart.HttpPostRequestDecoder.EndOfDataDecoderException;
import org.jboss.netty.handler.codec.http.multipart.HttpPostRequestDecoder.ErrorDataDecoderException;
import org.jboss.netty.handler.codec.http.multipart.HttpPostRequestDecoder.IncompatibleDataDecoderException;
import org.jboss.netty.handler.codec.http.multipart.HttpPostRequestDecoder.NotEnoughDataDecoderException;
import org.jboss.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;
import org.jboss.netty.handler.codec.http.multipart.InterfaceHttpData;
import org.jboss.netty.logging.InternalLogger;
import org.jboss.netty.logging.InternalLoggerFactory;
import org.jboss.netty.util.CharsetUtil;

public class HttpUploadServerHandler extends SimpleChannelUpstreamHandler {
	private static final InternalLogger logger = InternalLoggerFactory
			.getInstance(HttpUploadServerHandler.class);

	private static final String CARRAIGE_RETURN = "\r\n";

	private HttpPostRequestDecoder decoder;

	private boolean readingChunks;

	private HttpRequest request;

	private final StringBuilder responseContent = new StringBuilder();

	private static final HttpDataFactory factory = new DefaultHttpDataFactory(
			DefaultHttpDataFactory.MINSIZE);

	static {
		DiskFileUpload.deleteOnExitTemporaryFile = false;

		DiskFileUpload.baseDirectory = null;
		DiskAttribute.deleteOnExitTemporaryFile = false;

		DiskAttribute.baseDirectory = null;
	}

	private FileOutputStream fos;

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
		try {
			fos = new FileOutputStream("FileUpload.txt");
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) {
		if (null != decoder) {
			decoder.cleanFiles();
		}
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws URISyntaxException {
		if (!readingChunks) {
			if (null != decoder) {
				decoder.cleanFiles();
				decoder = null;
			}

			HttpRequest request = this.request = (HttpRequest) e.getMessage();
			URI uri = new URI(request.getUri());
			if (!uri.getPath().startsWith("/form")) {
				writeMenu(e);
				return;
			}

			try {
				decoder = new HttpPostRequestDecoder(factory, request);
			} catch (ErrorDataDecoderException e1) {
				e1.printStackTrace();
				responseContent.append(e1.getMessage());
				writeResponse(e.getChannel());
				Channels.close(e.getChannel());
				return;
			} catch (IncompatibleDataDecoderException e1) {
				// body가 없을 경우
				responseContent.append(e1.getMessage());
				responseContent.append(CARRAIGE_RETURN + CARRAIGE_RETURN
						+ "End of get content" + CARRAIGE_RETURN);
				writeResponse(e.getChannel());
				return;
			}

			if (request.isChunked()) {
				responseContent.append("chunks:");
				readingChunks = true;
			} else {
				readHttpDataAllReceive(e.getChannel());
				responseContent.append(CARRAIGE_RETURN + CARRAIGE_RETURN
						+ "End of not chunked content" + CARRAIGE_RETURN);
				writeResponse(e.getChannel());
			}
		} else {
			HttpChunk chunk = (HttpChunk) e.getMessage();
			try {
				decoder.offer(chunk);
				System.out.println("chunk offering..");
			} catch (ErrorDataDecoderException e1) {
				e1.printStackTrace();
				responseContent.append(e1.getMessage());
				writeResponse(e.getChannel());
				Channels.close(e.getChannel());
				return;
			}
//			 readHttpDataChunkByChunk(e.getChannel());
			if (chunk.isLast()) {
				readHttpDataAllReceive(e.getChannel());
				writeResponse(e.getChannel());
				readingChunks = false;
			}
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		logger.error(responseContent.toString(), e.getCause());
		e.getChannel().close();
	}

	private void readHttpDataChunkByChunk(Channel channel) {
		try {
			while (decoder.hasNext()) {
				InterfaceHttpData data = decoder.next();
				if (null != data) {
					writeHttpData(data);
				}
			}
		} catch (EndOfDataDecoderException e) {
			responseContent.append(CARRAIGE_RETURN + CARRAIGE_RETURN
					+ " end of content chunk by chunk" + CARRAIGE_RETURN
					+ CARRAIGE_RETURN);
		}
	}

	private void readHttpDataAllReceive(Channel channel) {
		List<InterfaceHttpData> datas = null;
		try {
			datas = decoder.getBodyHttpDatas();
		} catch (NotEnoughDataDecoderException e) {
			e.printStackTrace();
			responseContent.append(e.getMessage());
			writeResponse(channel);
			Channels.close(channel);
			return;
		}
		for (InterfaceHttpData data : datas) {
			writeHttpData(data);
		}
		responseContent.append(CARRAIGE_RETURN + CARRAIGE_RETURN
				+ " end of content at final end" + CARRAIGE_RETURN);
	}

	private void writeHttpData(InterfaceHttpData data) {
		if (data.getHttpDataType() == HttpDataType.Attribute) {
			Attribute attribute = (Attribute) data;
			String value = null;
			try {
				value = attribute.getValue();
			} catch (IOException e) {
				e.printStackTrace();
				// 클라이언트에게 에러메시지 출력
				return;
			}
			// body attribute 출력
		} else if (data.getHttpDataType() == HttpDataType.FileUpload) {
			FileUpload fileUpload = (FileUpload) data;
			if (fileUpload.isCompleted()) {
				try {
					byte[] b = fileUpload.get();
					fos.write(b);
					fos.flush();
					fos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				try { // 파일 내용 출력
					responseContent.append(fileUpload.getString(fileUpload
							.getCharset()));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			responseContent.append("\tFile to be continued but should not!"
					+ CARRAIGE_RETURN);
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

		Set<Cookie> cookies;
		String value = request.getHeader(HttpHeaders.Names.COOKIE);
		if (null == value) {
			cookies = Collections.emptySet();
		} else {
			CookieDecoder decoder = new CookieDecoder();
			cookies = decoder.decode(value);
		}
		if (!cookies.isEmpty()) {
			CookieEncoder cookieEncoder = new CookieEncoder(true);
			for (Cookie cookie : cookies) {
				cookieEncoder.addCookie(cookie);
				response.addHeader(HttpHeaders.Names.SET_COOKIE,
						cookieEncoder.encode());
				cookieEncoder = new CookieEncoder(true);
			}
		}

		ChannelFuture future = channel.write(response);

		if (close) {
			future.addListener(ChannelFutureListener.CLOSE);
		}
	}

	private void writeMenu(MessageEvent e) {
		// print several HTML forms
		// Convert the response content to a ChannelBuffer.
		responseContent.setLength(0);

		// create Pseudo Menu
		responseContent.append("<html>");
		responseContent.append("<head>");
		responseContent.append("<title>Netty Test Form</title>\r\n");
		responseContent.append("</head>\r\n");
		responseContent
				.append("<body bgcolor=white><style>td{font-size: 12pt;}</style>");

		responseContent.append("<table border=\"0\">");
		responseContent.append("<tr>");
		responseContent.append("<td>");
		responseContent.append("<h1>Netty Test Form</h1>");
		responseContent.append("Choose one FORM");
		responseContent.append("</td>");
		responseContent.append("</tr>");
		responseContent.append("</table>\r\n");

		// GET
		responseContent
				.append("<CENTER>GET FORM<HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");
		responseContent.append("<FORM ACTION=\"/formget\" METHOD=\"GET\">");
		responseContent
				.append("<input type=hidden name=getform value=\"GET\">");
		responseContent.append("<table border=\"0\">");
		responseContent
				.append("<tr><td>Fill with value: <br> <input type=text name=\"info\" size=10></td></tr>");
		responseContent
				.append("<tr><td>Fill with value: <br> <input type=text name=\"secondinfo\" size=20>");
		responseContent
				.append("<tr><td>Fill with value: <br> <textarea name=\"thirdinfo\" cols=40 rows=10></textarea>");
		responseContent.append("</td></tr>");
		responseContent
				.append("<tr><td><INPUT TYPE=\"submit\" NAME=\"Send\" VALUE=\"Send\"></INPUT></td>");
		responseContent
				.append("<td><INPUT TYPE=\"reset\" NAME=\"Clear\" VALUE=\"Clear\" ></INPUT></td></tr>");
		responseContent.append("</table></FORM>\r\n");
		responseContent
				.append("<CENTER><HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");

		// POST
		responseContent
				.append("<CENTER>POST FORM<HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");
		responseContent.append("<FORM ACTION=\"/formpost\" METHOD=\"POST\">");
		responseContent
				.append("<input type=hidden name=getform value=\"POST\">");
		responseContent.append("<table border=\"0\">");
		responseContent
				.append("<tr><td>Fill with value: <br> <input type=text name=\"info\" size=10></td></tr>");
		responseContent
				.append("<tr><td>Fill with value: <br> <input type=text name=\"secondinfo\" size=20>");
		responseContent
				.append("<tr><td>Fill with value: <br> <textarea name=\"thirdinfo\" cols=40 rows=10></textarea>");
		responseContent
				.append("<tr><td>Fill with file (only file name will be transmitted): <br> "
						+ "<input type=file name=\"myfile\">");
		responseContent.append("</td></tr>");
		responseContent
				.append("<tr><td><INPUT TYPE=\"submit\" NAME=\"Send\" VALUE=\"Send\"></INPUT></td>");
		responseContent
				.append("<td><INPUT TYPE=\"reset\" NAME=\"Clear\" VALUE=\"Clear\" ></INPUT></td></tr>");
		responseContent.append("</table></FORM>\r\n");
		responseContent
				.append("<CENTER><HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");

		// POST with enctype="multipart/form-data"
		responseContent
				.append("<CENTER>POST MULTIPART FORM<HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");
		responseContent
				.append("<FORM ACTION=\"/formpostmultipart\" ENCTYPE=\"multipart/form-data\" METHOD=\"POST\">");
		responseContent
				.append("<input type=hidden name=getform value=\"POST\">");
		responseContent.append("<table border=\"0\">");
		responseContent
				.append("<tr><td>Fill with value: <br> <input type=text name=\"info\" size=10></td></tr>");
		responseContent
				.append("<tr><td>Fill with value: <br> <input type=text name=\"secondinfo\" size=20>");
		responseContent
				.append("<tr><td>Fill with value: <br> <textarea name=\"thirdinfo\" cols=40 rows=10></textarea>");
		responseContent
				.append("<tr><td>Fill with file: <br> <input type=file name=\"myfile\">");
		responseContent.append("</td></tr>");
		responseContent
				.append("<tr><td><INPUT TYPE=\"submit\" NAME=\"Send\" VALUE=\"Send\"></INPUT></td>");
		responseContent
				.append("<td><INPUT TYPE=\"reset\" NAME=\"Clear\" VALUE=\"Clear\" ></INPUT></td></tr>");
		responseContent.append("</table></FORM>\r\n");
		responseContent
				.append("<CENTER><HR WIDTH=\"75%\" NOSHADE color=\"blue\"></CENTER>");

		responseContent.append("</body>");
		responseContent.append("</html>");

		ChannelBuffer buf = ChannelBuffers.copiedBuffer(
				responseContent.toString(), CharsetUtil.UTF_8);
		// Build the response object.
		HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1,
				HttpResponseStatus.OK);
		response.setContent(buf);
		response.setHeader(HttpHeaders.Names.CONTENT_TYPE,
				"text/html; charset=UTF-8");
		response.setHeader(HttpHeaders.Names.CONTENT_LENGTH,
				String.valueOf(buf.readableBytes()));
		// Write the response.
		e.getChannel().write(response);
	}
}