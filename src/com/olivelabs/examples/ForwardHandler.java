package com.olivelabs.examples;

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

public class ForwardHandler implements IHttpRequestHandler, ILifeCycle{
	private HttpClient httpClient = null;  
	   private String host = null;
	   private int port = -1;

	   ForwardHandler(String targetHost, int targetPort) {
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
	         httpClient.send(req, new ReverseHandler(exchange));
	      } catch (ConnectException ce) {
	         exchange.sendError(502, ce.getMessage());
	      }
	   }
	   public static void main(String args[]){
		   System.setProperty("org.xlightweb.showDetailedError", "true");

		
		try {
			int listenport = 8000;
			RequestHandlerChain chain = new RequestHandlerChain();
			chain.addLast(new LogFilter());
			chain.addLast(new ForwardHandler("www.google.com",80));

			HttpServer proxy = new HttpServer(listenport, chain);
			proxy.setAutoCompressThresholdBytes(Integer.MAX_VALUE);
			proxy.setAutoUncompress(false);

			proxy.run();
			

			//proxy.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   
		   
	   }
}
