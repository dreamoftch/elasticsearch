package com.tch.test.elasticsearch.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

public class ElasticSearchUtil {

	private static TransportClient client = null;
	
	static{
		try {
			client = TransportClient.builder().build()
					.addTransportAddress(new InetSocketTransportAddress(
							InetAddress.getByName("localhost"), 9300));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public static TransportClient client() {
		return client;
	}
	
	public static void release() {
		if (client != null) {
			client.close();
		}
	}
	

}
