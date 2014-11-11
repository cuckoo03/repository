package org.jboss.netty.example.http.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import javax.activation.MimetypesFileTypeMap;

import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelFutureProgressListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.DefaultFileRegion;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.FileRegion;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.frame.TooLongFrameException;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.jboss.netty.handler.ssl.SslHandler;
import org.jboss.netty.handler.stream.ChunkedFile;
import org.jboss.netty.util.CharsetUtil;

public class HttpStaticFileServerHandler extends SimpleChannelUpstreamHandler {
	private static final String HTTP_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";
	private static final String HTTP_DATE_GMT_TIMEZONE = "GMT";
	private static final int HTTP_CACHE_SECONDS = 60;
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
			throws IOException, ParseException {
		HttpRequest request = (HttpRequest) e.getMessage();
		if (request.getMethod() != HttpMethod.GET) {
			sendError(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED);
			return;
		}

		final String path = sanitizeUri(request.getUri());
		if (null == path) {
			sendError(ctx, HttpResponseStatus.FORBIDDEN);
			return;
		}

		File file = new File(path);
		if (file.isHidden() || !file.exists()) {
			sendError(ctx, HttpResponseStatus.FORBIDDEN);
			return;
		}
		
		if (!file.isFile()) {
			sendError(ctx, HttpResponseStatus.NOT_FOUND);
			return;
		}

		// Cache Validation
		String ifModifiedSince = request
				.getHeader(HttpHeaders.Names.IF_MODIFIED_SINCE);
		if ((ifModifiedSince != null) && (ifModifiedSince.length() != 0)) {
			SimpleDateFormat dateFormatter = new SimpleDateFormat(
					HTTP_DATE_FORMAT);
			Date ifModifiedSinceDate = dateFormatter.parse(ifModifiedSince);
			
			long ifModifiedSinceDateSeconds = ifModifiedSinceDate.getTime() / 1000;
			long fileLastModifiedSeconds = file.lastModified() / 1000;
			if (ifModifiedSinceDateSeconds == fileLastModifiedSeconds) {
				sendNotModified(ctx);
				return;
			}
		}

		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(file, "r");
		} catch (FileNotFoundException fnfe) {
			sendError(ctx, HttpResponseStatus.NOT_FOUND);
			return;
		}
		long fileLength = raf.length();

		HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1,
				HttpResponseStatus.OK);
		HttpHeaders.setContentLength(response, fileLength);
		setContentTypeHeader(response, file);
		setDateAndCacheHeaders(response, file);

		Channel ch = e.getChannel();

		ch.write(response);

		ChannelFuture writeFuture = null;
		// cannot use zero-copy with https
		if (ch.getPipeline().get(SslHandler.class) != null) {
			writeFuture = ch.write(new ChunkedFile(raf, 0, fileLength, 8192));
			writeFuture.addListener(new ChannelFutureProgressListener() {
				public void operationComplete(ChannelFuture future)
						throws Exception {
					System.out.println("server write operationComplete");
				}
				public void operationProgressed(ChannelFuture future,
						long amount, long current, long total) throws Exception {
					System.out.printf("%s %d / %d (+%d)%n", path, current,
							total, amount);
				}
			});
		} else { // use zero-copy
			final FileRegion region = new DefaultFileRegion(raf.getChannel(),
					0, fileLength);
			writeFuture = ch.write(region);
			writeFuture.addListener(new ChannelFutureProgressListener() {
				public void operationComplete(ChannelFuture future)
						throws Exception {
					System.out.println("server write operationComplete");
					region.releaseExternalResources();
				}
				public void operationProgressed(ChannelFuture future,
						long amount, long current, long total) throws Exception {
					System.out.printf("%s %d / %d (+%d)%n", path, current,
							total, amount);
				}
			});
		}

		if (!HttpHeaders.isKeepAlive(request)) {
			writeFuture.addListener(ChannelFutureListener.CLOSE);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		Channel ch = e.getChannel();
		Throwable cause = e.getCause();
		if (cause instanceof TooLongFrameException) {
			sendError(ctx, HttpResponseStatus.BAD_REQUEST);
			return;
		}

		cause.printStackTrace();
		if (ch.isConnected()) {
			sendError(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private String sanitizeUri(String uri) {
		// decode the path
		try {
			uri = URLDecoder.decode(uri, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			try {
				uri = URLDecoder.decode(uri, "ISO-8859-1");
			} catch (UnsupportedEncodingException e1) {
				throw new Error();
			}
		}

		uri = uri.replace('/', File.separatorChar);

		if (uri.contains(File.separator + ".")
				|| uri.contains("." + File.separator) || uri.startsWith(".")
				|| uri.endsWith(".")) {
			return null;
		}

		return System.getProperty("user.dir") + File.separator + uri;
	}

	private void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
		HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1,
				status);
		response.setHeader(HttpHeaders.Names.CONTENT_TYPE,
				"text/plain; charset=UTF-8");
//		response.setContent(ChannelBuffers.copiedBuffer(
//				"Failure:" + status.toString() + "\r\n", CharsetUtil.UTF_8));

		ctx.getChannel().write(response)
				.addListener(ChannelFutureListener.CLOSE);
	}
	
	private void sendNotModified(ChannelHandlerContext ctx) {
		HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, 
				HttpResponseStatus.NOT_MODIFIED);
		setDateHeader(response);
		
		 // Close the connection as soon as the error message is sent.
		ctx.getChannel().write(response)
				.addListener(ChannelFutureListener.CLOSE);
	}

	private void setDateHeader(HttpResponse response) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat(HTTP_DATE_FORMAT,
				Locale.US);
		dateFormatter.setTimeZone(TimeZone.getTimeZone(HTTP_DATE_GMT_TIMEZONE));
		
		Calendar time = new GregorianCalendar();
		response.setHeader(HttpHeaders.Names.DATE,
				dateFormatter.format(time.getTime()));
		
	}
	
	private void setContentTypeHeader(HttpResponse response, File file) {
		MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
		response.setHeader(HttpHeaders.Names.CONTENT_TYPE,
				mimeTypesMap.getContentType(file.getPath()));
	}

	private void setDateAndCacheHeaders(HttpResponse response, File file) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat(HTTP_DATE_FORMAT, Locale.US);
		dateFormatter.setTimeZone(TimeZone.getTimeZone(HTTP_DATE_GMT_TIMEZONE));
		
		// date header
		Calendar time = new GregorianCalendar();
		response.setHeader(HttpHeaders.Names.DATE, 
				dateFormatter.format(time.getTime()));

		// add cache headers
		time.add(Calendar.SECOND, HTTP_CACHE_SECONDS);
		response.setHeader(HttpHeaders.Names.EXPIRES, 
				dateFormatter.format(time.getTime()));
		response.setHeader(HttpHeaders.Names.CACHE_CONTROL, 
				"private, max-age=" + HTTP_CACHE_SECONDS);
		response.setHeader(HttpHeaders.Names.LAST_MODIFIED, 
				dateFormatter.format(new Date(file.lastModified())));
	}
}