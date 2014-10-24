package com.avro;

import java.net.InetSocketAddress;
import java.net.URL;

import org.apache.avro.ipc.HttpTransceiver;
import org.apache.avro.ipc.NettyTransceiver;
import org.apache.avro.ipc.SocketTransceiver;
import org.apache.avro.ipc.Transceiver;
import org.apache.avro.ipc.specific.SpecificRequestor;
import org.apache.avro.util.Utf8;

public class HelloClient {
	public static void main(String[] args) throws Exception {
		Transceiver client = new HttpTransceiver(new URL(
				"http://localhost:9090"));

//		NettyTransceiver client = new NettyTransceiver(new InetSocketAddress(9090));
		
//		SocketTransceiver client = new SocketTransceiver(new InetSocketAddress(
//				"127.0.0.1", 9090));
		
		HelloService proxy = SpecificRequestor.getClient(HelloService.class,
				client);
		Greeting result = proxy.hello(new Utf8("k"));
		System.out.println(result.greetingMessage.toString());
		client.close();
		System.out.println("closed");
	}
}
