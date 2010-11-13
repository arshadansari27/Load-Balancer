package com.olivelabs.loadbalancer.implementation;


import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;

public class ServerTest {

	static Server server;
	@BeforeClass
	public static void setUp() throws Exception {
		server = new Server(8888);
		server.setBalancer(new BalancerMock());
		server.startServer();
	}

	@Test
	public void testSendRequest() throws IOException, InterruptedException{
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod();
		method.setURI(new URI("http://localhost:8888/"));
		client.executeMethod(method);
		System.out.println("Client  Side");
		System.out.println(method.getStatusCode());
		
	}
	
	@Ignore
	@Test
	public void testRestart() throws Exception {
		Thread.currentThread().sleep(2000);
		server.reloadServer();
	}
	@AfterClass
	public static void tearDown() throws Exception {
		Thread.currentThread().sleep(5000);
		server.stopServer();
	}

}
