package com.olivelabs.loadbalancer.implementation;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import com.olivelabs.data.Request;
import com.olivelabs.data.Ri;
import com.olivelabs.loadbalancer.IServer;

/*
 * Server class that instantiates the server handler executor threads. 
 * Exposes methods to start or stop the load balancer. 
 */
public class Server implements IServer, Runnable {

	ServerHandler[] handlers;
	ExecutorService handlerExecutor;
	ExecutorService serverExecutor;
	ServerSocket server;
	int port;
	AtomicBoolean started;
	int poolSize;
	int currentHandler;
	ConcurrentLinkedQueue<Request> QCHIGH;
	ConcurrentLinkedQueue<Request> QCMEDIUM;
	ConcurrentLinkedQueue<Request> QCLOW;
	ConcurrentHashMap<Long, Ri> requests;

	public Server(int port, int poolSize) {
		started = new AtomicBoolean();
		this.port = port;
		this.poolSize = poolSize;
		currentHandler = 0;
	}
	public void setQCj(ConcurrentLinkedQueue<Request> QCHIGH,ConcurrentLinkedQueue<Request> QCMEDIUM,ConcurrentLinkedQueue<Request> QCLOW){
		this.QCHIGH = QCHIGH;
		this.QCMEDIUM = QCMEDIUM;
		this.QCLOW = QCLOW;
		
	}

	public void setRequestsTree(ConcurrentHashMap<Long, Ri> requests){
		this.requests = requests;
	}
	@Override
	public void startServer() throws Exception {
		started.set(true);
		serverExecutor = Executors.newFixedThreadPool(1);
		serverExecutor.execute(this);
	}

	@Override
	public void stopServer() throws Exception {
		started.set(false);
	}

	@SuppressWarnings("static-access")
	@Override
	public void reloadServer() throws Exception {
		stopServer();
		Thread.currentThread().sleep(2000);
		startServer();
	}

	private void setupResource() {
		if (port < 0) {
			throw new RuntimeException("Port is not Set!");
		}
		try {
			server = new ServerSocket(this.port);
			//server.setSoTimeout(5000);

		} catch (Throwable e) {
			e.printStackTrace();
			System.out.println("Load balancer cannot be started. Server failed to bind to given host:port configuration! Stopping now....");
			System.exit(1);
		}
	}

	private void releaseResources() {
		try {
			server.close();
			handlerExecutor.shutdownNow();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private ServerHandler getHander() {
		if (handlers.length < 1)
			return null;
		else {
			return handlers[(currentHandler++) % poolSize];
		}
	}

	@Override
	public void run() {
		handlers = new ServerHandler[this.poolSize];
		handlerExecutor = Executors.newFixedThreadPool(this.poolSize);
		if(this.QCHIGH==null || this.QCLOW==null || this.QCMEDIUM==null || this.requests==null){
			throw new RuntimeException("Please set the required classifier queues or the request probability list");
		}
		for (int i = 0; i < this.poolSize; i++) {
			handlers[i] = new ServerHandler();
			handlers[i].setQCj(QCHIGH, QCMEDIUM, QCLOW);
			handlers[i].setRequestsTree(requests);
			handlerExecutor.execute(handlers[i]);
		}

		setupResource();
		while (started.get()) {
			try {
				Socket socket = server.accept();
				getHander().serve(socket);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		releaseResources();
	}
}
