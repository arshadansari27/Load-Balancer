package com.olivelabs.loadbalancer.implementation;


import org.junit.Before;
import org.junit.Test;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import com.olivelabs.loadbalancer.IClient;

public class HttpServerTest {

	public static HttpServer server;
	public static HttpBalancer balancer;
    
    
	@Before
	public void setUp() throws Exception{
		server = new HttpServer(9090,10);
		balancer = null;
		server.setBalancer(balancer);
	}
	
	
	@Test
	public void testStop() throws Exception {
		server.start();
		server.stop();
	}
	
	
	@Test
	public void testSendingRequest() throws Exception{
		//fail("Some mock object problem that needs to be resolved!");
		server.start();
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
	    asyncHttpClient.prepareGet("http://localhost:9090/ ").execute(new AsyncCompletionHandler<Response>(){
	        
	        @Override
	        public Response onCompleted(Response  response) throws Exception{
	            System.out.println("Got the response");
	            System.out.println(response.getResponseBody());
	            return response;
	        }
	        
	        @Override
	        public void onThrowable(Throwable t){
	        	System.out.println(t.getMessage());
	        }
	    });
	    Thread.currentThread().sleep(2000);
        server.stop();
	}
}
