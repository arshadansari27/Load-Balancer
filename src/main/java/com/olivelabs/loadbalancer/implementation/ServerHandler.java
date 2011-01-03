package com.olivelabs.loadbalancer.implementation;

import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.olivelabs.loadbalancer.IBalancer;

public class ServerHandler implements Runnable {

	private List<Socket> socketList = new ArrayList<Socket>();
	private IBalancer balancer;

	public void setBalancer(IBalancer balancer) {
		this.balancer = balancer;
	}

	public void run() {
		while (true) {
			synchronized (socketList) {
				if (socketList.size() == 0 || socketList.isEmpty()) {
					try {
						socketList.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				Iterator<Socket> iterator = socketList.iterator();
				while (iterator.hasNext()) {
					handleSocket(iterator.next());
				}
				socketList.clear();
			}
		}
	}

	public void serve(Socket socket) {
		synchronized (socketList) {
			socketList.add(socket);
			socketList.notifyAll();
		}
	}

	public synchronized void handleSocket(Socket socket) {
		balancer.handle(socket);
	}

}
