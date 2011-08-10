package org.olivelabs.loadbalancer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.PriorityBlockingQueue;

public class Server implements Runnable{

	private Executor executor;
	private ServerSocket serverSocket;
	private boolean running = false;
	private PriorityBlockingQueue<Request> queue;
	private ConcurrentHashMap<Integer, RequestClass> requestClass;
	public Server(int port, Executor executor, PriorityBlockingQueue<Request> queue, ConcurrentHashMap<Integer, RequestClass> requestClass) throws IOException{
		this.serverSocket = new ServerSocket(port);
		this.executor = executor;
		this.queue = queue;
		this.requestClass = requestClass;
		running = true;
	}
	
	
	public void run() {
		while(running){
			try {
				Socket socket = serverSocket.accept();
				executor.execute(new RequestHandler(socket, queue, requestClass));
			} catch (IOException e) {
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
