package com.olivelabs.loadbalancer.implementation;


import com.olivelabs.loadbalancer.IBalancer;
import com.olivelabs.loadbalancer.IClient;
import com.olivelabs.loadbalancer.IServer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer implements IServer {

	private InetAddress lbHostAddress;
	private int lbPort;
	private HttpServerHelper helper;
	private ServerSocketChannel lbServerChannel;
	private Selector selector;
	private Executor executor;
	private IBalancer balancer;
	
	

	public HttpServer(int port,int poolSize) throws IOException {
		System.out.println(InetAddress.getLocalHost());
		this.lbHostAddress = InetAddress.getLocalHost();
		
		this.lbPort = port;
		executor = Executors.newSingleThreadExecutor();
		this.helper = new HttpServerHelper();	
		
	}

	
	@Override
	public void setBalancer(IBalancer balancer) {
		this.balancer  = balancer;
		if(this.helper != null) this.helper.setBalancer(this.balancer);
	}


	@Override
	public void start() throws Exception{
		this.selector = SelectorProvider.provider().openSelector();

		// Create a new non-blocking server socket channel
		this.lbServerChannel = ServerSocketChannel.open();
		lbServerChannel.configureBlocking(false);

		// Bind the server socket to the specified address and port
		InetSocketAddress isa = new InetSocketAddress(this.lbHostAddress,this.lbPort);
		System.out.println(isa.getHostName()+" "+isa.getAddress()+" "+isa.getPort()+" "+isa.isUnresolved());
		lbServerChannel.socket().bind(isa);

		// Register the server socket channel, indicating an interest in 
		// accepting new connections
		lbServerChannel.register(this.selector , SelectionKey.OP_ACCEPT);
		
		helper.setChannelAndSelector( lbServerChannel, selector);
		
		executor.execute(helper);
	}

	@Override
	public void stop()  throws Exception{
		helper.stop();
		lbServerChannel.close();
		lbServerChannel = null;
	}




	public void restart() throws Exception{
		
		this.stop();
		Thread.currentThread().wait(5000);
		this.start();
		
	}


	
}
