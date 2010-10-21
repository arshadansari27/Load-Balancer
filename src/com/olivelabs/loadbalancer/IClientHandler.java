package com.olivelabs.loadbalancer;

import org.apache.mina.core.session.IoSession;

public interface IClientHandler {
	public void sessionOpened(IoSession session);
	public void messageReceived(IoSession session, Object message);
	public void exceptionCaught(IoSession session, Throwable cause);
	public void setRequest(Object request);
	public void setSessionServer(IoSession sessionServer);
	public boolean isFinished();
}
