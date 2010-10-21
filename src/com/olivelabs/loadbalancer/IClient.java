package com.olivelabs.loadbalancer;

import org.apache.mina.core.session.IoSession;

public interface IClient{
	
	public boolean isFinished();
	public boolean sendRequest(Object request,IoSession serverSession) throws Exception;
}
