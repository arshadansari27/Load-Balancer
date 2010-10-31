package com.olivelabs.loadbalancer.implementation;

import java.io.IOException;
import java.io.PrintStream;

import com.olivelabs.loadbalancer.IClient;

public class HttpWorker implements Runnable {

	private IClient client;

	public HttpWorker(IClient client) {
		this.client  = client;
	}

	@Override
	public void run() {
		
	
	}
}
