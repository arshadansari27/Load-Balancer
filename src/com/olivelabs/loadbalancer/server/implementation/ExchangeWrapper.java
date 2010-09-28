package com.olivelabs.loadbalancer.server.implementation;

import org.xlightweb.IHttpExchange;
import org.xlightweb.IHttpRequest;

import com.olivelabs.loadbalancer.server.Exchange;

public class ExchangeWrapper implements Exchange{

	private IHttpExchange exchange;
	public ExchangeWrapper(IHttpExchange exchange) throws Exception{
		if (exchange==null) throw new Exception("Request Not Set");
		this.exchange = exchange;
	}
	
	public Object getExchange() throws Exception{
		if (exchange==null) throw new Exception("Request Not Set");
		return exchange;
	}
}
