package com.olivelabs.loadbalancer;

import org.apache.mina.core.service.IoHandlerAdapter;



public interface IServer{
	public void start() throws Exception;
	public void stop() throws Exception;
	public void restart() throws Exception;
	
	public IoHandlerAdapter getRequestHandler();



	public void setRequestHandler(IServerHandler requestHandler);

	
	
}
