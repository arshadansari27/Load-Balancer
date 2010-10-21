package com.olivelabs.loadbalancer;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

public interface IServerHandler {

	public void exceptionCaught( IoSession session, Throwable cause ) throws Exception;

    public void messageReceived( IoSession session, Object message ) throws Exception;

    public void sessionIdle( IoSession session, IdleStatus status ) throws Exception;
}
