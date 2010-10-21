package com.olivelabs.loadbalancer.implementation;

import java.util.logging.Logger;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.LoggerFactory;

import com.olivelabs.loadbalancer.IClientHandler;

public class HttpClientHandler extends IoHandlerAdapter  implements IClientHandler {

	private final static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(HttpClientHandler.class);
	private boolean finished;
	private IoSession sessionServer;
	private Object request;
	

	
	public boolean isFinished() {
		return finished;
     }
	
	@Override
	public void exceptionCaught(IoSession session, Throwable cause) {
		session.close(true);
		sessionServer.close(true);
	}

	@Override
	public void messageReceived(IoSession session, Object message) {
		System.out.println("Response to client"+message.toString());
		sessionServer.write(message);
		finished = true;
	}

	@Override
	public void sessionOpened(IoSession session) {
		System.out.println(request+".... sent from client");
		 session.write(request);
		 
	}


	@Override
	public void setRequest(Object request) {
		this.request = request;	
	}


	public IoSession getSessionServer() {
		return sessionServer;
	}


	public void setSessionServer(IoSession sessionServer) {
		this.sessionServer = sessionServer;
	}

}
