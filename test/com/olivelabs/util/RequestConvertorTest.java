package com.olivelabs.util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.simpleframework.http.Response;
import org.simpleframework.http.core.Container;
import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;
import org.simpleframework.util.thread.Scheduler;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Request;
import com.ning.http.client.RequestBuilder;
import com.ning.http.client.AsyncHttpClient.BoundRequestBuilder;

public class RequestConvertorTest {
	RequestConvertor converter;
	Scheduler scheduler;
	Connection connection;
	SocketAddress address;
	org.simpleframework.http.Request requestSimple;
	Object semaphore;
	@Before
	public void setUp() throws Exception {
		semaphore = new Object();
		address = new InetSocketAddress(8888);
		scheduler = new Scheduler(10);
		
		connection = new SocketConnection(new Container() {
			
			

			@Override
			public void handle(org.simpleframework.http.Request request,
					Response response) {
				try {
					com.ning.http.client.Request requestClient = new RequestBuilder(
							RequestConvertor.getRequestType(request.getMethod())		
					).build();
					
					
						requestSimple = request;
						
						
						
										
						
					 PrintStream body = response.getPrintStream();
						
				         long time = System.currentTimeMillis();

				         response.set("Content-Type", "text/plain");
				         response.set("Server", "HelloWorld/1.0 (Simple 4.0)");
				         response.setDate("Date", time);
				         response.setDate("Last-Modified", time);

				         body.println("Reply Response");
				         body.close();
				         synchronized(semaphore){
				        	 semaphore.notify();
				        	 System.out.println("Notified....");
				         }
					} catch (Throwable e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			}
		});
		connection.connect(address);
	}

	@After
	public void tearDown() throws Exception {
		connection.close();
	}

	@Test
	public void testCopyRequest() throws Throwable {
	   
	   AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
	   BoundRequestBuilder requestBuilder =  asyncHttpClient.prepareGet("http://localhost:8888/ ");
	   Request request = requestBuilder.build();
	   requestBuilder.execute();
	   synchronized(semaphore){
		   System.out.println("Waiting....");
		   semaphore.wait(10000);
	   }
	   com.ning.http.client.Request requestCopied = RequestConvertor.copyRequest(requestSimple, "www.google.com", "80");
	   System.out.println(requestSimple.getAddress().getScheme()+"://" +
	   		"" + requestSimple.getAddress().getDomain() +
	   		":" + requestSimple.getAddress().getPort() +
	   		"" + requestSimple.getAddress().getPath() +
	   		"?"+requestSimple.getAddress().getQuery());
	   System.out.println(requestCopied.getUrl());

	}

}
