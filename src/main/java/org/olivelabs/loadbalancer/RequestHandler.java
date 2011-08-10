package org.olivelabs.loadbalancer;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

public class RequestHandler implements Runnable {
	
	private Socket socket;
	private PriorityBlockingQueue<Request> queue;
	private ConcurrentHashMap<Integer, RequestClass> requestClass;
	private Request request;
	
	public RequestHandler(Socket socket, PriorityBlockingQueue<Request> queue, ConcurrentHashMap<Integer, RequestClass> requestClass) throws IOException{
		this.socket = socket;
		this.queue = queue;
		this.requestClass = requestClass;
	}
	
	public void run() {
		 
		RequestExtractor extractor = new RequestExtractor(socket);
		String requestText = extractor .getRequest();
		String urlPath = extractor .getURLPath();
		Double classLevel = getURLClassLevel(urlPath);
		this.request = new Request(this.socket, classLevel, urlPath, requestText);
		this.queue.offer(request);
	}

	private Double getURLClassLevel(String urlPath){
		RequestClass reqClass = requestClass.get(urlPath.hashCode());
		if(reqClass == null){
			reqClass = new RequestClass(urlPath);
			requestClass.put(urlPath.hashCode(), reqClass);
		}	
		reqClass.increaseRequestCount();
		return reqClass.getClassLevel();
	}
}
