package com.olivelabs.loadbalancer;

import org.xlightweb.RequestHandlerChain;
import org.xlightweb.server.HttpServer;

import com.olivelabs.data.Metric;
import com.olivelabs.loadbalancer.implementation.HttpBalancer;
import com.olivelabs.loadbalancer.logging.LogRequestHandler;
import com.olivelabs.loadbalancer.server.HttpRequestHandler;
import com.olivelabs.routing.RoutingAlgorithm;

public class ClusterMainTest {
	public static void main(String[] args) {
		System.setProperty("org.xlightweb.showDetailedError", "true");

		
		try {
			
			RequestHandlerChain chain = new RequestHandlerChain();
			chain.addLast(new LogRequestHandler());
			HttpServer testServer1 =  new HttpServer(9090, chain);
			testServer1.setAutoCompressThresholdBytes(Integer.MAX_VALUE);
			testServer1.setAutoUncompress(false);
			testServer1.start();
			
			
			RequestHandlerChain chain2 = new RequestHandlerChain();
			chain2.addLast(new LogRequestHandler());
			HttpServer testServer2 =  new HttpServer(9091, chain2);
			testServer2.setAutoCompressThresholdBytes(Integer.MAX_VALUE);
			testServer2.setAutoUncompress(false);
			testServer2.start();
			//proxy.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   
		   
	}
}
