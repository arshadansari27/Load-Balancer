package com.olivelabs.loadbalancer.implementation;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.olivelabs.data.INode;
import com.olivelabs.loadbalancer.IBalancer;
import com.olivelabs.loadbalancer.IClient;
import com.olivelabs.loadbalancer.IClientHandler;

public class HttpClient implements IClient {

	private INode node;
	private IBalancer balancer;
	private IClientHandler clientHandler;
	private NioSocketConnector connector;
	private static final long CONNECT_TIMEOUT = 30*1000L;
	
	
	public HttpClient(IBalancer balancer){
		this.balancer = balancer;
		this.clientHandler = new HttpClientHandler();
		connector = new NioSocketConnector();
		connector.setConnectTimeoutMillis(CONNECT_TIMEOUT);
		connector.getFilterChain().addLast( "logger", new LoggingFilter() );
		connector.getFilterChain().addLast( "codec", new ProtocolCodecFilter( new TextLineCodecFactory( Charset.forName( "UTF-8" ))));
		connector.setHandler((IoHandler) clientHandler);
		
	}
	
	public boolean sendRequest(Object request,IoSession serverSession) throws Exception{
		clientHandler.setRequest(request);
		clientHandler.setSessionServer(serverSession);
		node = balancer.getNode();
		IoSession session;
		for (;;) {
            try {
                ConnectFuture future = connector.connect(new InetSocketAddress(node.getHost(), node.getPort().intValue()));
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
        return false;
	}
	
	
	public boolean isFinished() {
		return clientHandler.isFinished();
     }

	
}
