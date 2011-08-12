package org.olivelabs.loadbalancer;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.PriorityBlockingQueue;

import org.olivelabs.loadbalancer.dispatcher.DispatcherFactor;
import org.olivelabs.loadbalancer.dispatcher.DispatcherType;

public class RequestListener implements Runnable {

	private Executor executor;
	private boolean running = false;
	private PriorityBlockingQueue<Request> queue;
	private ConcurrentHashMap<Integer, RequestClass> requestClass;
	private PriorityBlockingQueue<Node> nodes;
	public RequestListener(Executor executor, PriorityBlockingQueue<Request> queue, ConcurrentHashMap<Integer, RequestClass> requestClass, PriorityBlockingQueue<Node> nodes) throws IOException{
		this.executor = executor;
		this.queue = queue;
		this.requestClass = requestClass;
		this.nodes = nodes;
		running = true;
	}
	
	
	public void run() {
		while(running){
			try {
				Request request = queue.take();
				System.out.println("*** Listener received request...");
				DispatcherFactor.DISPATCHER_TYPE = DispatcherType.HANDLE_REQUEST;
				executor.execute(DispatcherFactor.getDispatcher(requestClass, nodes, request));
				System.out.println("*** Dispatched request...");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(Thread.currentThread().isInterrupted() && !running) break;
		}
		
	}
	
	public void stop(){
		this.running = false;
	}

}
