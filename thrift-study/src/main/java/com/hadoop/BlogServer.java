package com.hadoop;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TTransportException;

import com.jetty.BlogHttpServer;
import com.thrift.BlogService;

public class BlogServer {
	public static final int PORT = 9099;
	public static BlogServer blogServer;

	private String hostName;
	private BlogServiceHandler blogService;

	public BlogServer(String hdfsUri) throws IOException {
		blogService = new BlogServiceHandler(this, hdfsUri);
	}

	public String getHostName() {
		return hostName;
	}

	public int getPort() {
		return PORT;
	}

	public BlogServiceHandler getBlogService() {
		return blogService;
	}

	public void startBlogServer() throws Exception {
		startHttpServer();
		startThriftServer();
	}

	public void startHttpServer() throws Exception {
		BlogHttpServer server = new BlogHttpServer("webapps", 8080);
		server.start();
		System.err.println("BlogHttpServer started");
	}

	public void startThriftServer() throws UnknownHostException,
			TTransportException {
		this.hostName = InetAddress.getLocalHost().getHostName();

		final TNonblockingServerSocket socket = new TNonblockingServerSocket(
				PORT);
		final BlogService.Processor processor = new BlogService.Processor(
				blogService);
		final TServer server = new THsHaServer(processor, socket,
				new TFramedTransport.Factory(), new TBinaryProtocol.Factory());
		System.out.println("BlogServer started:" + PORT);
		server.serve();
	}

	public static void main(String[] args) throws Exception {
		args = new String[] { "hdfs://192.168.1.108:9000" };
		if (args.length < 1) {
			System.out.println("Usage java BlogServer <hdfs>");
			System.out.println(" hdfs option: hdfs://127.0.0.1:9000");
			System.exit(0);
		}
		blogServer = new BlogServer(args[0]);
		blogServer.startBlogServer();
	}
}
