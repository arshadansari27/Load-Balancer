package com.olivelabs.loadbalancer.implementation;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.*;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.junit.Before;
import org.junit.Test;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import com.olivelabs.data.INode;
import com.olivelabs.loadbalancer.IBalancer;
import com.olivelabs.loadbalancer.IClient;
import com.olivelabs.loadbalancer.IServerHandler;
import com.olivelabs.loadbalancer.implementation.HttpServerHandler;

public class HttpServerTest {

	public static HttpServer server;
	public static IServerHandler requestHandler;
    private static final long CONNECT_TIMEOUT = 30*1000L; 
    
    
	@Before
	public void setUp() throws Exception{
		server = new HttpServer(9090,10);
		IClient client = new HttpClientMock();
		INode n1 = createMock(INode.class);
		expect(n1.getHost()).andReturn("localhost");
		expect(n1.getPort()).andReturn(new Long(9999));
		replay(n1);
		IoSession session = createMock(IoSession.class);
		replay(session);
		
		
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
	        	System.out.println("Shit happened!");
	        }
	    });
	    Thread.currentThread().sleep(20000);
        server.stop();
	}
}
