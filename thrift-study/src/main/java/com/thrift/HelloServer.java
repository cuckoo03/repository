package com.thrift;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TTransportException;

public class HelloServer {

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("Usage java HelloServer <port>");
			System.exit(0);
		}
		int port = Integer.parseInt(args[0]);
		try {
			final TNonblockingServerSocket socket = new TNonblockingServerSocket(
					port);
			final HelloService.Processor processor = new HelloService.Processor(
					new HelloHandler());
			final TServer server = new THsHaServer(processor, socket,
					new TFramedTransport.Factory(),
					new TBinaryProtocol.Factory());

			System.out.println("HelloServer started(port:" + port + ")");
			server.serve();
		} catch (TTransportException e) {
			e.printStackTrace();
		}
	}
}
