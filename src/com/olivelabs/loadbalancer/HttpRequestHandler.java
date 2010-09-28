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

public class HttpRequestHandler implements  IHttpRequestHandler, ILifeCycle{
	private HttpClient httpClient = null;  
	   private String host = null;
	   private int port = -1;

	   HttpRequestHandler(String targetHost, int targetPort) {
	      host = targetHost;
	      port = targetPort;
	   }
	   
	   public void onInit() {
	      httpClient = new HttpClient();
	      httpClient.setAutoHandleCookies(false);  // cookie auto handling has to be deactivated!
	      httpClient.setFollowsRedirect(false);
	      httpClient.setAutoUncompress(false);
	   }
	   
	   public void onDestroy() throws IOException {
	      httpClient.close();
	   }

	   @Execution(Execution.MULTITHREADED)
	   public void onRequest(IHttpExchange exchange) throws IOException, BadMessageException {

	      IHttpRequest req = exchange.getRequest();

	      // reset address (Host header will be update automatically)
	      URL url = req.getRequestUrl();
	      req.setRequestUrl(new URL(url.getProtocol(), host, port, url.getFile()));

	      // perform further proxy issues (via header, cache, remove hop-by-hop headers, ...)
	      // ...


	      // .. and forward the request
	      try {
	         httpClient.send(req, new HttpResponseHandler(exchange));
	      } catch (ConnectException ce) {
	         exchange.sendError(502, ce.getMessage());
	      }
	   }
}
