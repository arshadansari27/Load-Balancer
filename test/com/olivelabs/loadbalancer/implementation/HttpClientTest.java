package com.olivelabs.loadbalancer.implementation;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import org.apache.mina.core.session.IoSession;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;


import com.ning.http.client.Request;
import com.ning.http.client.RequestBuilder;
import com.ning.http.client.RequestType;
import com.olivelabs.data.INode;
import com.olivelabs.data.Metric;
import com.olivelabs.loadbalancer.IBalancer;


public class HttpClientTest {

	
	public static HttpClient client;
	public static HttpClientHandler clientHandler;
	public static IoSession session;
	@Before
	public void setUp() throws Exception{
		IBalancer balancer = createMock(IBalancer.class);
		INode n1 = new com.olivelabs.data.Node("www.finicity.com", "80", new Metric());
		expect(balancer.getNode()).andReturn(n1);
		replay(balancer);
		clientHandler = new HttpClientHandler();
		client = new HttpClient(balancer);
		
	}
	
	
	@Test
	public void testSendRequest() throws Exception{
		RequestBuilder builder = new RequestBuilder(RequestType.GET);
		builder.addHeader("content-type", "text/html");
		builder.setUrl("http://www.google.com/");
		Request request = builder.build();
		client.sendRequest(request,null);
		while(!client.isFinished())
			Thread.currentThread().yield();
		//Thread.currentThread().wait(10000);
	}
	
	@Test
	public void testGetNewURL() throws Exception{
		System.out.println(client.getNewURL("http://www.google.com/test", "localhost", new Long(80)));		
	}
	
}
