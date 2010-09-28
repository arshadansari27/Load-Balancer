package com.olivelabs.loadbalancer.server.implementation;

import java.io.IOException;
import java.net.UnknownHostException;

import org.xlightweb.RequestHandlerChain;
import org.xlightweb.server.HttpServer;

import com.olivelabs.loadbalancer.server.Server;

public class SimpleServer implements Server {
	private HttpServer httpServer;

	public SimpleServer(int listenport, RequestHandlerChain chain) throws UnknownHostException, IOException {
		httpServer = new HttpServer(listenport,chain);
	}

	@Override
	public void setAutoCompressThresholdBytes(int maxValue) {
		httpServer.setAutoCompressThresholdBytes(maxValue);
		
	}

	@Override
	public void setAutoUncompress(boolean autoUncompress) {
		httpServer.setAutoUncompress(autoUncompress);
	}

	@Override
	public void run() {
		httpServer.run();
		
	}

	
	
	
}
