package com.olivelabs.loadbalancer;

import java.io.IOException;
import java.net.UnknownHostException;

import org.xlightweb.IWebHandler;
import org.xlightweb.RequestHandlerChain;
import org.xlightweb.server.HttpServer;
import org.xsocket.IDestroyable;

import com.olivelabs.examples.ForwardHandler;
import com.olivelabs.examples.LogFilter;
import com.olivelabs.loadbalancer.server.RequestHandler;
import com.olivelabs.loadbalancer.server.Server;
import com.olivelabs.loadbalancer.server.implementation.SimpleServer;

public class MainClass {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.setProperty("org.xlightweb.showDetailedError", "true");

		
		try {
			int listenport = 8000;
			RequestHandlerChain chain = new RequestHandlerChain();
			RequestHandler requestHandler = new HttpRequestHandler("www.google.com",80);
			chain.addLast(new LogFilter());
			chain.addLast(requestHandler);

			Server proxy =  new SimpleServer(listenport, chain);
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
