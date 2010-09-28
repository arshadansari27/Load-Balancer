package com.olivelabs.loadbalancer.server;

import org.xlightweb.IHttpRequest;

public interface Request{
	Object getRequest() throws Exception;
}
