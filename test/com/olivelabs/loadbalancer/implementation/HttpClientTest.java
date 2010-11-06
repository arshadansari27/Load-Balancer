package com.olivelabs.loadbalancer.implementation;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.net.InetAddress;

import junit.framework.Assert;


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
		client = new HttpClient(InetAddress.getByName("finicity.com"),80);
		
	}
	
	
	@Test
	public void testSendRequest() throws Exception{
		for(int i =0; i<3; i++){
		RspHandler handler = new RspHandler();
		client.send("GET / HTTP/1.0\n\n".getBytes(), handler);
		byte[] response = handler.waitForResponse();
		Assert.assertNotNull(response);
		Assert.assertTrue(response.length>1);
		for(int index = 0; index < response.length; index++){
			System.out.print((char)response[index]);
		}
		for(int index = 0; index < 10; index++){
			System.out.print("*");
		}
		System.out.println("*");
		System.out.println(response.length);
		}
	}
	
	
	
}
