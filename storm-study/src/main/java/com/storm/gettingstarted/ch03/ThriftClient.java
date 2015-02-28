package com.storm.gettingstarted.ch03;

import org.apache.thrift7.protocol.TBinaryProtocol;
import org.apache.thrift7.transport.TFramedTransport;
import org.apache.thrift7.transport.TSocket;
import org.apache.thrift7.transport.TTransportException;

import backtype.storm.generated.Nimbus.Client;

public class ThriftClient {
	private static final String STORM_UI_NODE = "192.168.1.101";

	public Client getClient() {
		TSocket socket = new TSocket(STORM_UI_NODE, 6627);
		TFramedTransport tFrameTransport = new TFramedTransport(socket);
		TBinaryProtocol tBinaryProtocol = new TBinaryProtocol(tFrameTransport);
		Client client = new Client(tBinaryProtocol);

		try {
			tFrameTransport.open();
		} catch (TTransportException e) {
			e.printStackTrace();
			throw new RuntimeException("Error occurred thrift server.");
		}
		return client;
	}
}
