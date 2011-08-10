package org.olivelabs.loadbalancer;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.PriorityBlockingQueue;

public class RequestListener implements Runnable {

	private Executor executor;
	private boolean running = false;
	private PriorityBlockingQueue<Request> queue;
	private ConcurrentHashMap<Integer, RequestClass> requestClass;
	public RequestListener(Executor executor, PriorityBlockingQueue<Request> queue, ConcurrentHashMap<Integer, RequestClass> requestClass) throws IOException{
		this.executor = executor;
		this.queue = queue;
		this.requestClass = requestClass;
		running = true;
	}
	
	
	public void run() {
		while(running){
			try {
				Request request = queue.take();
				//executor.execute(new RequestHandler(socket, queue, requestClass));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				if(!running) break;
			}
			if(Thread.currentThread().isInterrupted() && !running) break;
		}
		
	}
	
	public void stop(){
		this.running = false;
	}

}
