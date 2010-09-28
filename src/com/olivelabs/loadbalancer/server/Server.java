package com.olivelabs.loadbalancer.server;

import org.xlightweb.server.IHttpServer;

public interface Server{

	void setAutoCompressThresholdBytes(int maxValue);

	void setAutoUncompress(boolean autoUncompress);
	
	void run();

}
