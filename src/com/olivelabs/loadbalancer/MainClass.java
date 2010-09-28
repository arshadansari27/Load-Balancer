package com.olivelabs.loadbalancer;


import org.xlightweb.RequestHandlerChain;
import org.xlightweb.server.HttpServer;
import com.olivelabs.examples.LogFilter;
public class MainClass {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.setProperty("org.xlightweb.showDetailedError", "true");

		
		try {
			int listenport = 8000;
			RequestHandlerChain chain = new RequestHandlerChain();
			HttpRequestHandler requestHandler = new HttpRequestHandler("www.google.com",80);
			chain.addLast(new LogFilter());
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
