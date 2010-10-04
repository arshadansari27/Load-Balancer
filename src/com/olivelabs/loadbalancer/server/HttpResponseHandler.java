package com.olivelabs.loadbalancer.server;


import java.io.IOException;
import java.net.SocketTimeoutException;

import org.xlightweb.IHttpExchange;
import org.xlightweb.IHttpResponse;
import org.xlightweb.IHttpResponseHandler;
import org.xlightweb.IHttpSocketTimeoutHandler;
import org.xlightweb.InvokeOn;
import org.xsocket.Execution;

import com.olivelabs.data.IMetric;
import com.olivelabs.data.INode;



public class HttpResponseHandler implements IHttpResponseHandler, IHttpSocketTimeoutHandler{
		//public static final int SIZE_OF_CHAR = 8;
	   private IHttpExchange exchange = null;
	   private INode node;
	   public HttpResponseHandler(IHttpExchange exchange) {
	      this.exchange = exchange;
	   }

	   @Execution(Execution.NONTHREADED)
	   @InvokeOn(InvokeOn.HEADER_RECEIVED)
	   public void onResponse(IHttpResponse resp) throws IOException {

	      System.out.println("In ResponseHandler...");
		  double servedRequestSizeInMB = (resp.getContentLength()*1.0)/(1024*1024); 
		  node.setNumberOfRequestServed(Long.valueOf(1L));
		  node.setRequestServedSizeInMB(Double.valueOf(servedRequestSizeInMB));
		  System.out.println("Sending Exchange back....");
	      exchange.send(resp);
	      System.out.println("Exchange sent!!....");
	   }

	   @Execution(Execution.NONTHREADED)
	   public void onException(IOException ioe) {
	      exchange.sendError(500, ioe.toString());
	   }

	   @Execution(Execution.NONTHREADED)
	   public void onException(SocketTimeoutException stoe) {
	      exchange.sendError(504, stoe.toString());
	   }
}
