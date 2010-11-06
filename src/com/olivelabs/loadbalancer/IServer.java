package com.olivelabs.loadbalancer;

import org.apache.mina.core.service.IoHandlerAdapter;



public interface IServer{
	public void start() throws Exception;
	public void stop() throws Exception;
	public void setBalancer(IBalancer balancer);
}
