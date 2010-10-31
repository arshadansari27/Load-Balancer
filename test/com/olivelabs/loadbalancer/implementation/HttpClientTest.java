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
	
	@Before
	public void setUp() throws Exception{
		IBalancer balancer = createMock(IBalancer.class);
		INode n1 = new com.olivelabs.data.Node("www.finicity.com", "80", new Metric());
		expect(balancer.getNode()).andReturn(n1);
		replay(balancer);
		client = new HttpClient(balancer);
		
	}
	
	
	@Test
	public void testSendRequest() throws Exception{
		RspHandler handler = new RspHandler();
		client.send("GET / HTTP/1.1\n\n".getBytes(), handler);
		System.out.println(handler.waitForResponse());
		
	}
	
	
	
}
