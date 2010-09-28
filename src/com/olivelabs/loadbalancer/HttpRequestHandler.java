package com.olivelabs.loadbalancer;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;
import java.net.UnknownHostException;

import org.xlightweb.BadMessageException;
import org.xlightweb.IHttpExchange;
import org.xlightweb.IHttpRequest;
import org.xlightweb.IHttpRequestHandler;
import org.xlightweb.RequestHandlerChain;
import org.xlightweb.client.HttpClient;
import org.xlightweb.server.HttpServer;
import org.xsocket.Execution;
import org.xsocket.ILifeCycle;

import com.olivelabs.loadbalancer.client.implementation.SimpleHTTPClient;
import com.olivelabs.loadbalancer.server.Exchange;
import com.olivelabs.loadbalancer.server.Request;
import com.olivelabs.loadbalancer.server.RequestHandler;
import com.olivelabs.loadbalancer.server.implementation.ExchangeWrapper;
import com.olivelabs.loadbalancer.server.implementation.RequestWrapper;
import com.olivelabs.loadbalancer.server.implementation.SimpleResponseHandler;

public class HttpRequestHandler implements RequestHandler{
	   private SimpleHTTPClient httpClient = null;  
	   private String host = null;
	   private int port = -1;

	   public HttpRequestHandler(String targetHost, int targetPort) {
	      host = targetHost;
	      port = targetPort;
	   }
	   
	   public void onInit() {
	      httpClient = new SimpleHTTPClient();
	      httpClient.setAutoHandleCookies(false);  // cookie auto handling has to be deactivated!
	      httpClient.setFollowsRedirect(false);
	      httpClient.setAutoUncompress(false);
	   }
	   
	   public void onDestroy() throws IOException {
	      httpClient.close();
	   }

	   @Execution(Execution.MULTITHREADED)
	   public void onRequest(IHttpExchange exchange) throws IOException, BadMessageException {

	      
	       try {
	    	  IHttpRequest req = exchange.getRequest();
		      
		      // reset address (Host header will be update automatically)
		      URL url = req.getRequestUrl();
		      req.setRequestUrl(new URL(url.getProtocol(), host, port, url.getFile()));
		      Request requestWrapped = new RequestWrapper(req);
	         httpClient.send(requestWrapped, new SimpleResponseHandler(new ExchangeWrapper(exchange)));
	      } catch (ConnectException ce) {
	         exchange.sendError(502, ce.getMessage());
	      }
	      catch (Exception e){
	    	  exchange.sendError(500, e.getMessage());
	      }
	   }
	  
}
