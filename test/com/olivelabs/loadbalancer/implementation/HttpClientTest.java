package com.olivelabs.loadbalancer.implementation;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import org.apache.mina.core.session.IoSession;
import org.junit.Before;
import org.junit.Test;

import com.olivelabs.data.INode;
import com.olivelabs.loadbalancer.IBalancer;


public class HttpClientTest {

	
	public static HttpClient client;
	public static HttpClientHandler clientHandler;
	public static IoSession session;
	@Before
	public void setUp() throws Exception{
		IBalancer balancer = createMock(IBalancer.class);
		session = createMock(IoSession.class);
		INode n1 = createMock(INode.class);
		expect(n1.getHost()).andReturn("www.finicity.com");
		expect(n1.getPort()).andReturn(new Long(80));
		replay(n1);
		expect(balancer.getNode()).andReturn(n1);
		replay(balancer);
		expect(session.write(new Object())).andReturn(null);
		expect(session.close(true)).andReturn(null);
		clientHandler = new HttpClientHandler();
		clientHandler.setSessionServer(session);
		client = new HttpClient(balancer);
		
	}
	
	@Test
	public void testSendRequest() throws Exception{
		client.sendRequest("GET / HTTP1.0\n\n",session);
	}
	
}
