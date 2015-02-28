package com.storm.gettingstarted.ch03;

import org.apache.thrift7.TException;

import backtype.storm.generated.Nimbus.Client;

public class NimbusConfiguration {
	public void printNimbusStats() {
		ThriftClient thriftClient = new ThriftClient();
		Client client = thriftClient.getClient();
		try {
			String nimbusConfiguration = client.getNimbusConf();
			System.out.println("--------------------");
			System.out.println("nimbus conf:" + nimbusConfiguration);
			System.out.println("--------------------");
		} catch (TException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		new NimbusConfiguration().printNimbusStats();
	}
}
