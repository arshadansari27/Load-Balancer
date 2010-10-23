package com.olivelabs.loadbalancer.implementation;

import org.simpleframework.http.*;
import org.simpleframework.util.thread.Scheduler;

import com.olivelabs.example.AsynchronousService.Task;
import com.olivelabs.loadbalancer.IClient;
import com.olivelabs.loadbalancer.IServerHandler;

public class HttpServerHandler implements IServerHandler {
	
	private IClient client;
	private Scheduler queue;
	
	public HttpServerHandler(Scheduler queue){
		this.queue = queue;
	}
	
	public IClient getClient() {
		return client;
	}



	public void setClient(IClient client) {
		this.client = client;
	}


	public Scheduler getQueue() {
		return queue;
	}



	public void setQueue(Scheduler queue) {
		this.queue = queue;
	}



	@Override
	public void handle(Request request, Response response) {
			HttpWorker worker = new HttpWorker(request, response,client);
			queue.execute(worker);
	}
	
	
	
	    
	   
}
