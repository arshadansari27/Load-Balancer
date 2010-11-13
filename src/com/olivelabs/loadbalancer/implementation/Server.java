package com.olivelabs.loadbalancer.implementation;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.olivelabs.loadbalancer.IBalancer;
import com.olivelabs.loadbalancer.IServer;

public class Server implements IServer, Runnable {

	IBalancer balancer;
	ServerHandler[] handlers;
	ExecutorService handlerExecutor;
	ExecutorService serverExecutor;
	ServerSocket server;
	int port;
	boolean started;
	int poolSize;
	int currentHandler;

	public Server(int port) {
		started = true;
		this.port = port;
		poolSize = 1;
		currentHandler = 0;
	}

	@Override
	public void startServer() throws Exception {
		serverExecutor = Executors.newSingleThreadExecutor();
		serverExecutor.execute(this);

	}

	@Override
	public void stopServer() throws Exception {
		started = false;
		handlerExecutor.shutdown();
		handlerExecutor.shutdownNow();
		serverExecutor.shutdown();
		serverExecutor.shutdownNow();
	}

	@Override
	public void setBalancer(IBalancer balancer) {
		this.balancer = balancer;

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
			// server.setSoTimeout(20000);

		} catch (Throwable e) {
			e.printStackTrace();
			throw new RuntimeException("Server failure: " + e.getMessage());
		}
	}

	private void releaseResources() throws IOException {
		server.close();
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

		for (int i = 0; i < this.poolSize; i++) {
			handlers[i] = new ServerHandler();
			handlers[i].setBalancer(this.balancer);
			handlerExecutor.execute(handlers[i]);
		}

		setupResource();
		while (true) {
			try {
				System.out.println("Server listening.....");
				Socket socket = server.accept();
				System.out.println("Server got request...");
				getHander().serve(socket);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// releaseResources();
	}
}
