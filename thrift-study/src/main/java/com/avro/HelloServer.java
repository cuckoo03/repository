package com.avro;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.avro.AvroRemoteException;
import org.apache.avro.ipc.HttpServer;
import org.apache.avro.ipc.NettyServer;
import org.apache.avro.ipc.SocketServer;
import org.apache.avro.ipc.specific.SpecificResponder;
import org.apache.avro.util.Utf8;

public class HelloServer {
	public static class HelloServiceImpl implements HelloService {
		@Override
		public Greeting hello(CharSequence name) throws AvroRemoteException,
				GreetingException {
			Greeting greeting = new Greeting();
			greeting.greetingMessage = new Utf8("hello " + name);
			return greeting;
		}
	}

	private static void startHttpServer() throws IOException {
		SpecificResponder r = new SpecificResponder(HelloService.class,
				new HelloServiceImpl());
		final HttpServer server = new HttpServer(r, 9090);
		System.out.println("http server started");
		synchronized (server) {
			try {
				server.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private static void startSocketServer() throws IOException {
		@SuppressWarnings("deprecation")
		final SocketServer server = new SocketServer(new SpecificResponder(
				HelloService.class, new HelloServiceImpl()),
				new InetSocketAddress(9090));

		System.out.println("socket server started");
		synchronized (server) {
			try {
				server.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private static void startNettyServer() {
		final NettyServer server = new NettyServer(new SpecificResponder(
				HelloService.class, new HelloServiceImpl()),
				new InetSocketAddress(9090));
		System.out.println("netty server started");
		synchronized (server) {
			try {
				server.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			System.out.println("Usage: java HelloServer <type(http|socket)");
			System.exit(0);
		}
		if ("socket".equals(args[0])) {
			startSocketServer();
		} else if ("netty".equals(args[0])) {
			startNettyServer();
		} else {
			startHttpServer();
		}
		System.out.println("server stoped");
	}
}
