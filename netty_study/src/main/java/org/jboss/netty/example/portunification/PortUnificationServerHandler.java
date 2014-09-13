package org.jboss.netty.example.portunification;

import javax.net.ssl.SSLEngine;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.example.factorial.BigIntegerDecoder;
import org.jboss.netty.example.factorial.FactorialServerHandler;
import org.jboss.netty.example.factorial.NumberEncoder;
import org.jboss.netty.example.http.snoop.HttpServerRequestHandler;
import org.jboss.netty.example.securechat.SecureChatSslContextFactory;
import org.jboss.netty.handler.codec.compression.ZlibDecoder;
import org.jboss.netty.handler.codec.compression.ZlibEncoder;
import org.jboss.netty.handler.codec.compression.ZlibWrapper;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.jboss.netty.handler.codec.http.HttpContentCompressor;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;
import org.jboss.netty.handler.ssl.SslHandler;

public class PortUnificationServerHandler extends FrameDecoder {
	private final boolean detectSsl;
	private final boolean detectGzip;

	public PortUnificationServerHandler() {
		this(true, true);
	}

	private PortUnificationServerHandler(boolean detectSsl, boolean detectGzip) {
		this.detectSsl = detectSsl;
		this.detectGzip = detectGzip;
	}

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel,
			ChannelBuffer buffer) throws Exception {
		if (buffer.readableBytes() < 2) {
			ctx.getChannel().close();
			return null;
		}

		final int magic1 = buffer.getUnsignedByte(buffer.readerIndex());
		final int magic2 = buffer.getUnsignedByte(buffer.readerIndex() + 1);

		if (isSsl(magic1)) {
			enableSsl(ctx);
		} else if (isGzip(magic1, magic2)) {
			enableGzip(ctx);
		} else if (isHttp(magic1, magic2)) {
			switchToHttp(ctx);
		} else if (isFactorial(magic1)) {
			switchToFactorial(ctx);
		} else {
			buffer.skipBytes(buffer.readableBytes());
			ctx.getChannel().close();
			return null;
		}

		return buffer.readBytes(buffer.readableBytes());
	}

	private void switchToFactorial(ChannelHandlerContext ctx) {
		ChannelPipeline pipeline = ctx.getPipeline();
		pipeline.addLast("decoder", new BigIntegerDecoder());
		pipeline.addLast("encoder", new NumberEncoder());
		pipeline.addLast("handler", new FactorialServerHandler());
		pipeline.remove(this);
	}

	private boolean isFactorial(int magic1) {
		return magic1 == 'F';
	}

	private void switchToHttp(ChannelHandlerContext ctx) {
		ChannelPipeline pipeline = ctx.getPipeline();
		pipeline.addLast("decoder", new HttpRequestDecoder());
		pipeline.addLast("encoder", new HttpResponseEncoder());
		pipeline.addLast("deflater", new HttpContentCompressor());
		pipeline.addLast("handler", new HttpServerRequestHandler());
		pipeline.remove(this);
	}

	private boolean isHttp(int magic1, int magic2) {
		return magic1 == 'G' && magic2 == 'E' || // GET
				magic1 == 'P' && magic2 == 'O' || // POST
				magic1 == 'P' && magic2 == 'U' || // PUT
				magic1 == 'H' && magic2 == 'E' || // HEAD
				magic1 == 'O' && magic2 == 'P' || // OPTIONS
				magic1 == 'P' && magic2 == 'A' || // PATCH
				magic1 == 'D' && magic2 == 'E' || // DELETE
				magic1 == 'T' && magic2 == 'R' || // TRACE
				magic1 == 'C' && magic2 == 'O'; // CONNECT
	}

	private void enableGzip(ChannelHandlerContext ctx) {
		ChannelPipeline pipeline = ctx.getPipeline();
		pipeline.addLast("gzipdeflater", new ZlibEncoder(ZlibWrapper.GZIP));
		pipeline.addLast("gzipinflater", new ZlibDecoder(ZlibWrapper.GZIP));
		pipeline.addLast("unificationB", new PortUnificationServerHandler(
				detectSsl, false));
		pipeline.remove(this);
	}

	private boolean isGzip(int magic1, int magic2) {
		if (detectGzip) {
			return magic1 == 31 && magic2 == 139;
		}
		return false;
	}

	private void enableSsl(ChannelHandlerContext ctx) {
		ChannelPipeline pipeline = ctx.getPipeline();

		SSLEngine engine = SecureChatSslContextFactory.getServerContext()
				.createSSLEngine();
		engine.setUseClientMode(false);

		pipeline.addLast("ssl", new SslHandler(engine));
		pipeline.addLast("unificationA", new PortUnificationServerHandler(
				false, detectSsl));
		pipeline.remove(this);
	}

	private boolean isSsl(int magic1) {
		if (detectSsl) {
			switch (magic1) {
			case 20:
			case 21:
			case 22:
			case 23:
			case 255:
				return true;
			default:
				return magic1 >= 128;
			}
		}
		return false;
	}
}