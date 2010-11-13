package com.olivelabs.loadbalancer;

import org.apache.mina.core.service.IoHandlerAdapter;



public interface IServer{
	public void startServer() throws Exception;
	public void stopServer() throws Exception;
	public void reloadServer() throws Exception;
	public void setBalancer(IBalancer balancer);
}
