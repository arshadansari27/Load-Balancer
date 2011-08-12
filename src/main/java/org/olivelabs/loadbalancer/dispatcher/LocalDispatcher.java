package org.olivelabs.loadbalancer.dispatcher;

import java.util.concurrent.PriorityBlockingQueue;

import org.olivelabs.loadbalancer.Node;
import org.olivelabs.loadbalancer.Request;

public class LocalDispatcher implements Dispatcher{

	private Request request;
	public LocalDispatcher(PriorityBlockingQueue<Node> nodes, Request request){
		this.request = request;
	}
	public void run(){
		System.out.println("***************\nThe received request is :\n "+request.REQUEST_TEXT);
		System.out.println("***************");
	}
}
