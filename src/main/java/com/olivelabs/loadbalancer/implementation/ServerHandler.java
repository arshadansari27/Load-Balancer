package com.olivelabs.loadbalancer.implementation;

import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.olivelabs.data.Priority;
import com.olivelabs.data.Request;
import com.olivelabs.data.Ri;

public class ServerHandler implements Runnable {

	private ConcurrentLinkedQueue<Socket> socketList = new ConcurrentLinkedQueue<Socket>();
	private ConcurrentLinkedQueue<Request> QCHIGH;
	private ConcurrentLinkedQueue<Request> QCMEDIUM;
	private ConcurrentLinkedQueue<Request> QCLOW;
	private ConcurrentHashMap<Long, Ri> requests;
	
	
	public void setQCj(ConcurrentLinkedQueue<Request> QCHIGH,ConcurrentLinkedQueue<Request> QCMEDIUM,ConcurrentLinkedQueue<Request> QCLOW){
		this.QCHIGH = QCHIGH;
		this.QCMEDIUM = QCMEDIUM;
		this.QCLOW = QCLOW;
		
	}

	public void setRequestsTree(ConcurrentHashMap<Long, Ri> requests){
		this.requests = requests;
	}
	
	public void run() {
		while (true) {
				if (socketList.size() == 0 || socketList.isEmpty()) {
					try {
						synchronized(socketList){
							socketList.wait();
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				Iterator<Socket> iterator = socketList.iterator();
				while (iterator.hasNext()) {
					handleSocket(iterator.next());
				}
				synchronized(socketList){
					socketList.clear();
				}
		}
	}

	public void serve(Socket socket) {
		synchronized(socketList){
			socketList.add(socket);
			socketList.notifyAll();
		}
	}

	public void handleSocket(Socket socket) {
		Request request = null;
		try {
			request = RequestURLExtractor.getURL(socket);
			Ri ri = null;
			Ri.totalRequestCount += 1d; 
			if(requests.containsKey(request.getHashCode())){
				ri = requests.get(request.getHashCode());
				ri.requestCount += 1d;
			
			}
			else{
				ri = new Ri();
				ri.hashCode = request.getHashCode();
				requests.put(ri.hashCode, ri);
				ri.requestCount = 1d;
			}
			Priority priority = ri.getPriority();
			if(priority == Priority.HIGH)
				QCHIGH.add(request);
			if(priority == Priority.MEDIUM)
				QCMEDIUM.add(request);
			if(priority == Priority.LOW)
				QCLOW.add(request);
			
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

}
