package com.olivelabs.loadbalancer.implementation;


import com.olivelabs.loadbalancer.IServerHandler;
import com.olivelabs.loadbalancer.IServer;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;

import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

public class HttpServer implements IServer {

	private int port;
	private String serverAddress;
	private IoAcceptor acceptor;
	private IoHandlerAdapter requestHandler;
	public HttpServer(String address, int port){
		this.port = port;
		this.serverAddress = address;
	}
	
	
	
	public IoHandlerAdapter getRequestHandler() {
		return requestHandler;
	}



	public void setRequestHandler(IServerHandler requestHandler) {
		this.requestHandler = (IoHandlerAdapter) requestHandler;
	}



	@Override
	public void restart()  throws Exception{
		this.stop();
		this.start();
	}

	@Override
	public void start() throws Exception{
        try {
        	this.acceptor = new NioSocketAcceptor();
    		acceptor.getFilterChain().addLast( "logger", new LoggingFilter() );
            acceptor.getFilterChain().addLast( "codec", new ProtocolCodecFilter( new TextLineCodecFactory( Charset.forName( "UTF-8" ))));
       
            // Attach the business logic to the server
            if(this.requestHandler == null) 
            	throw new Exception("Request Handler is null!");
            else
            	acceptor.setHandler(this.requestHandler);

            // Configurate the buffer size and the iddle time
            acceptor.getSessionConfig().setReadBufferSize( 2048 );
            acceptor.getSessionConfig().setIdleTime( IdleStatus.BOTH_IDLE, 10 );
			acceptor.bind( new InetSocketAddress(this.port) );
		} catch (Exception e) {
			System.out.println("Error while starting the load balancing sever!");
			e.printStackTrace();
			throw new Exception("Server start up failed!");
		}

	}

	@Override
	public void stop()  throws Exception{
		this.acceptor.unbind();
		this.acceptor.dispose();

	}

	
}
