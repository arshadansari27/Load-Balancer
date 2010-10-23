package com.olivelabs.loadbalancer;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.simpleframework.http.core.Container;

public interface IServerHandler extends Container{

	public void setClient(IClient client);
}
