package com.olivelabs.util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.core.Container;
import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;
import org.simpleframework.util.thread.Scheduler;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Part;
import com.ning.http.client.RequestBuilder;
import com.olivelabs.loadbalancer.IServerHandler;
import com.olivelabs.loadbalancer.implementation.HttpServerHandler;

public class ContainerRequestConvertorTest {

	private RequestConvertor converter;
	private Scheduler scheduler;
	private Connection connection;
	private SocketAddress address;
	
	@Before
	public void setUp() throws Exception {
		address = new InetSocketAddress(8888);
		scheduler = new Scheduler(10);
		
		connection = new SocketConnection(new Container() {
			
			@Override
			public void handle(Request request, Response response) {
				try {
				
				
				 com.ning.http.client.Request requestClient = RequestConvertor.copyRequest(request,  "www.google.com", "80");
				
					
					
					System.out.println(requestClient.getUrl());
					
					
					
				 PrintStream body = response.getPrintStream();
					
			         long time = System.currentTimeMillis();

			         response.set("Content-Type", "text/plain");
			         response.set("Server", "HelloWorld/1.0 (Simple 4.0)");
			         response.setDate("Date", time);
			         response.setDate("Last-Modified", time);

			         body.println("Reply Response");
			         body.close();
					
				} catch (Throwable e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		connection.connect(address);
		Thread.currentThread().sleep(2000);
	}

	@After
	public void tearDown() throws Exception {
		connection.close();
	}

	@Test
	public void testCopyRequest() throws Throwable {
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
		AsyncCompletionHandler<Response> responseHandler = new AsyncCompletionHandler<Response>(){
	        
	      
	        @Override
	        public void onThrowable(Throwable t){
	        	System.out.println("Shit happened!");
	        }

			@Override
			public Response onCompleted(com.ning.http.client.Response response)
					throws Exception {
				 System.out.println("Got the response");
		            System.out.println(response.getResponseBody());
				return null;
			}
	    };
	    asyncHttpClient.prepareGet("http://localhost:8888/Get/theworld?who=me ").execute(responseHandler);
	    asyncHttpClient.preparePost("http://localhost:8888/Post/theworld?who=me ").execute(responseHandler);
	    Thread.currentThread().sleep(2000);
        
	}

}
