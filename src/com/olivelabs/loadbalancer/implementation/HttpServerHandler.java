package com.olivelabs.loadbalancer.implementation;

import java.net.URL;
import java.util.Date;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import com.olivelabs.data.INode;
import com.olivelabs.loadbalancer.IBalancer;
import com.olivelabs.loadbalancer.IClient;
import com.olivelabs.loadbalancer.IServerHandler;

public class HttpServerHandler extends IoHandlerAdapter implements IServerHandler {
	
	private IClient client;
	
	public HttpServerHandler(IClient client) {
		this.client = client;
	}
	
	
	 @Override
	    public void exceptionCaught( IoSession session, Throwable cause ) throws Exception
	    {
	        cause.printStackTrace();
	    }

	    /**
	     * If the message is 'quit', we exit by closing the session. Otherwise,
	     * we return the current date.
	     */
	    @Override
	    public void messageReceived( IoSession session, Object message ) throws Exception
	    {      
	    	String request = message.toString();
	    	client.sendRequest(message, session);
	    	while(!client.isFinished());
	    	session.close(true);
	        
	    }

	    /**
	     * On idle, we just write a message on the console
	     */
	    @Override
	    public void sessionIdle( IoSession session, IdleStatus status ) throws Exception
	    {
	        System.out.println( "IDLE " + session.getIdleCount( status ));
	    }
}
