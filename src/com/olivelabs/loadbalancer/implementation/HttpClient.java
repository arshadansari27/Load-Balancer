package com.olivelabs.loadbalancer.implementation;


import java.net.InetAddress;
import java.util.Map.Entry;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.mina.core.session.IoSession;


import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.Cookie;
import com.ning.http.client.Part;
import com.ning.http.client.ProxyServer;
import com.ning.http.client.RequestBuilder;
import com.ning.http.client.RequestType;
import com.ning.http.client.Response;

import com.ning.http.client.Request;

import com.olivelabs.data.INode;
import com.olivelabs.loadbalancer.IBalancer;
import com.olivelabs.loadbalancer.IClient;
import com.olivelabs.loadbalancer.IClientHandler;

public class HttpClient implements IClient {

	private INode node;
	private IBalancer balancer;
	private IClientHandler clientHandler;
	private static final long CONNECT_TIMEOUT = 30*1000L;
	private Executor executor;
	private boolean finished;
	public HttpClient(IBalancer balancer){
		this.balancer = balancer;
		executor = Executors.newSingleThreadExecutor();
		
	}
	
	
	
	@Override
	public void send(byte[] data, RspHandler handler) throws Exception {
		INode node = balancer.getNode();
		String host = node.getHost();
		InetAddress hostAddress = InetAddress.getByName(host);
		Long port = node.getPort();
		clientHandler = new HttpClientHandler(hostAddress, port.intValue());
		clientHandler.call(data,handler);		
		executor.execute(clientHandler);
		
	}

	
	
	
	
}
