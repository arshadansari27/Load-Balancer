package com.olivelabs.loadbalancer;

import java.nio.channels.SocketChannel;

import com.olivelabs.loadbalancer.implementation.HttpServerHelper;


public interface IServerHandler {

	public void setClient(IClient client);
	public void processData(HttpServerHelper helper, SocketChannel socketChannel, byte[] data, int numRead) throws Exception;
	
}
