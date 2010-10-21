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
		server = new HttpServer("localhost", 9090);
		IClient client = createMock(IClient.class);
		INode n1 = createMock(INode.class);
		expect(n1.getHost()).andReturn("localhost");
		expect(n1.getPort()).andReturn(new Long(9999));
		replay(n1);
		IoSession session = createMock(IoSession.class);
		
		replay(session);
		expect(client.sendRequest("GET / HTTP1.0\n\n", session)).andReturn(true);
		replay(client);
		requestHandler = new HttpServerHandler(client);
		server.setRequestHandler(requestHandler);
		
	}
	
	
	
	@Test
	public void testRestart() throws Exception {
		server.start();
		server.restart();
		server.stop();
	}

	

	@Test
	public void testStop() throws Exception {
		server.start();
		server.stop();
	}
	
	
	@Test
	public void testSendingRequest() throws Exception{
		fail("Some mock object problem that needs to be resolved!");
		server.start();
		NioSocketConnector connector = new NioSocketConnector();
		connector.setConnectTimeoutMillis(CONNECT_TIMEOUT);
		connector.getFilterChain().addLast( "codec", new ProtocolCodecFilter( new TextLineCodecFactory( Charset.forName( "UTF-8" ))));
		connector.getFilterChain().addLast("logger", new LoggingFilter());
        connector.setHandler(new IoHandlerAdapter(){
        	 @Override
        	 public void sessionOpened(IoSession session) {
        		 session.write("GET / HTTP1.0\n\n");
        		
        	 }
        	 
        	 @Override
        	 public void messageReceived(IoSession session, Object message) {
        		 System.out.println(message.toString());
        	 }
        });
        
        
        
        
        IoSession session;
        for (;;) {
            try {
                ConnectFuture future = connector.connect(new InetSocketAddress("localhost", 9090));
                future.awaitUninterruptibly();
                session = future.getSession();
                break;
            } catch (RuntimeIoException e) {
                System.err.println("Failed to connect.");
                e.printStackTrace();
                Thread.sleep(5000);
            }
        }

        // wait until the summation is done
        session.getCloseFuture().awaitUninterruptibly();
        connector.dispose();
        server.stop();
	}
}
