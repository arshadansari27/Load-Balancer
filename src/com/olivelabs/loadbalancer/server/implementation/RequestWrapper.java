package com.olivelabs.loadbalancer.server.implementation;

import org.xlightweb.IHttpRequest;

import com.olivelabs.loadbalancer.server.Request;

public class RequestWrapper implements Request{
	private IHttpRequest request;
	public RequestWrapper(IHttpRequest req) throws Exception{
		if (req==null) throw new Exception("Request Not Set");
		this.request = req;
	}
	@Override
	public Object getRequest() throws Exception{
		if (request==null) throw new Exception("Request Not Set");
		return request;
	}
}
