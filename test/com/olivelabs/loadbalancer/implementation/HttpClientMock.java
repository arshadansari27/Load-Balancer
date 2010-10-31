package com.olivelabs.loadbalancer.implementation;


import org.simpleframework.http.Response;

import com.ning.http.client.Request;
import com.olivelabs.example.RspHandler;
import com.olivelabs.loadbalancer.IClient;

public class HttpClientMock implements IClient {


	@Override
	public void send(byte[] data,
			com.olivelabs.loadbalancer.implementation.RspHandler handler)
			throws Exception {
		byte[] header = ("HTTP/1.1 200 OK \n\n").getBytes();
		handler.handleResponse(header);
		// Look up the handler for this channel
		
		
	}
	
	

}
