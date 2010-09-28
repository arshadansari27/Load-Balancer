package com.olivelabs.loadbalancer.server.implementation;

import java.io.IOException;
import java.net.SocketTimeoutException;

import org.xlightweb.IHttpExchange;
import org.xlightweb.IHttpResponse;



import com.olivelabs.loadbalancer.HttpResponseHandler;
import com.olivelabs.loadbalancer.server.Exchange;
import com.olivelabs.loadbalancer.server.Response;
import com.olivelabs.loadbalancer.server.ResponseHandler;

public class SimpleResponseHandler implements ResponseHandler{

	
	private HttpResponseHandler responseHandler;
	
	public SimpleResponseHandler(Exchange exchange) throws Exception {
	      responseHandler = new HttpResponseHandler((IHttpExchange)exchange.getExchange());
	   }
	
	public HttpResponseHandler getResponseHandler(){
		return responseHandler;
	}
	

}
