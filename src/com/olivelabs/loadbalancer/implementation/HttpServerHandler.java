package com.olivelabs.loadbalancer.implementation;


import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;

import com.olivelabs.loadbalancer.IClient;
import com.olivelabs.loadbalancer.IServerHandler;

public class HttpServerHandler implements IServerHandler {
	private IClient client;
	
	public HttpServerHandler(){
		
	}
	
	
	public IClient getClient() {
		return client;
	}



	public void setClient(IClient client) {
		this.client = client;
	}


	
	public void processData(HttpServerHelper helper, SocketChannel socketChannel, byte[] data, int numRead) throws Exception{
		byte[] dataCopy = new byte[numRead];
		System.arraycopy(data, 0, dataCopy, 0, numRead);
		ServerDataEvent dataEvent = new ServerDataEvent(helper, socketChannel, dataCopy);
		
		RspHandler handler = new RspHandler();
		
		
		client.send(dataEvent.data, handler);
		
		dataEvent.server.send(dataEvent.socket, handler.waitForResponse() );
		
		System.out.println(dataEvent.data);
		
		
	}
	   
}
