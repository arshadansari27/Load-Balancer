package com.olivelabs.loadbalancer.implementation;


import com.olivelabs.example.AsynchronousService;
import com.olivelabs.loadbalancer.IClient;
import com.olivelabs.loadbalancer.IServerHandler;
import com.olivelabs.loadbalancer.IServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import org.simpleframework.http.core.Container;
import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;
import org.simpleframework.util.thread.Scheduler;

public class HttpServer implements IServer {

	private int port;
	private int poolSize;
	private Scheduler scheduler;
	private Connection connection;
	private SocketAddress address;
	private IServerHandler requestHandler;
	
	
	

	public HttpServer(int port,int poolSize) throws IOException {
		this.port = port;
		this.poolSize = poolSize;
		address = new InetSocketAddress(this.port);
		scheduler = new Scheduler(this.poolSize);
		requestHandler = new HttpServerHandler(scheduler);
		connection = new SocketConnection(requestHandler);
	}

	
	@Override
	public void start() throws Exception{
			connection.connect(address);

	}

	@Override
	public void stop()  throws Exception{
		connection.close();
	}




	@Override
	public void setClient(IClient client) {
		requestHandler.setClient(client);
	}

	

	
}
