package com.olivelabs.run;


import org.xlightweb.RequestHandlerChain;
import org.xlightweb.server.HttpServer;

import com.olivelabs.loadbalancer.IBalancer;
import com.olivelabs.loadbalancer.implementation.HttpBalancer;
import com.olivelabs.loadbalancer.logging.LogRequestHandler;
import com.olivelabs.loadbalancer.server.HttpRequestHandler;
public class MainClass {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.setProperty("org.xlightweb.showDetailedError", "true");

		
		try {
			int listenport = 8000;
			RequestHandlerChain chain = new RequestHandlerChain();
			IBalancer balancer = new HttpBalancer();
			HttpRequestHandler requestHandler = new HttpRequestHandler(balancer);
			chain.addLast(new LogRequestHandler());
			chain.addLast(requestHandler);

			HttpServer proxy =  new HttpServer(listenport, chain);
			proxy.setAutoCompressThresholdBytes(Integer.MAX_VALUE);
			proxy.setAutoUncompress(false);
			
			proxy.run();
			

			//proxy.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   
		   
	}

}
