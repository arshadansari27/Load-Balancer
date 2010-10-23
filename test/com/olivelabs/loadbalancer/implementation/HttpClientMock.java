package com.olivelabs.loadbalancer.implementation;


import org.simpleframework.http.Response;

import com.ning.http.client.Request;
import com.olivelabs.loadbalancer.IClient;

public class HttpClientMock implements IClient {

	boolean finished = false;
	@Override
	public boolean isFinished() {
		// TODO Auto-generated method stub
		return finished;
	}
	@Override
	public boolean sendRequest(Request request, Response response)
			throws Exception {
		System.out.println("Send Request from HTTPCLIENT:" + request);
		finished = true;
		return true;
	}
	@Override
	public boolean sendRequest(String request, Response response)
			throws Exception {
		System.out.println("Send Request from HTTPCLIENT:" + request);
		finished = true;
		return true;
	}

	

}
