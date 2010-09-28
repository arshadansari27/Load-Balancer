package com.olivelabs.loadbalancer.server;

import java.io.IOException;
import java.net.SocketTimeoutException;

import org.xlightweb.IHttpExchange;
import org.xlightweb.IHttpResponseHandler;
import org.xlightweb.IHttpSocketTimeoutHandler;

import com.olivelabs.loadbalancer.HttpResponseHandler;


public interface ResponseHandler{
	
	public HttpResponseHandler getResponseHandler();
}
