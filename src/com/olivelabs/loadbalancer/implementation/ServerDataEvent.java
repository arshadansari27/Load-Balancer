package com.olivelabs.loadbalancer.implementation;
import java.nio.channels.SocketChannel;

class ServerDataEvent {
	public HttpServerHelper server;
	public SocketChannel socket;
	public byte[] data;
	
	public ServerDataEvent(HttpServerHelper server, SocketChannel socket, byte[] data) {
		this.server = server;
		this.socket = socket;
		this.data = data;
	}
}