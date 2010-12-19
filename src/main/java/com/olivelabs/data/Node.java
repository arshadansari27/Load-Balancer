package com.olivelabs.data;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;

import com.olivelabs.loadbalancer.IClient;
import com.olivelabs.loadbalancer.implementation.Client;


public class Node implements INode {

	private Integer id;
	private String host;
	private Long port;
	private IMetric metric;
	private static int count = 0;
	private IClient client;
	private BlockingQueue<Socket> requestList;
	private boolean started;
	
	public Node(String host, String port, Metric metric)
			throws UnknownHostException {
		this.id = 1000 + count++;
		started = true;
		this.host = host;
		this.port = Long.parseLong(port);
		this.metric = metric;
		this.client = new Client(InetAddress.getByName(host),
				this.port.intValue());
		this.client.setMetrics(metric);
		requestList = new LinkedBlockingQueue<Socket>();
		
	}

	public String getHost() {
		return host;
	}

	public Long getPort() {
		return port;
	}

	public IMetric getMetric() {
		return metric;
	}

	public void setMetric(IMetric metric) {
		this.metric = metric;
	}

	public int hashCode() {
		return id.intValue();

	}

	public Integer getId() {

		return this.id;
	}

	@Override
	public void setNumberOfRequestServed(Long value) {
		metric.setNumberOfRequestServed(value);
	}

	public void setRequestServedSizeInMB(Double value) {
		metric.setRequestServedSizeInMB(value);
	}

	@Override
	public void handleRequest(Socket socket) throws Exception {
		synchronized (requestList) {
			requestList.add(socket);
			requestList.notifyAll();
		}
	}

	@Override
	public void run() {
		while (started) {

			synchronized (requestList) {
				if (requestList.isEmpty() || requestList.size()==0) {
					try {

						requestList.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			Iterator<Socket> iterator = requestList.iterator();
			while(iterator.hasNext()) {
				try {
					System.out.println("Node: "+this.getId());
					this.client.handleRequest(iterator.next());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			requestList.clear();

		}
	}

	public void start(){
		started = true;
	}

	public void stop(){
		started = false;
	}
	
	public boolean isStarted(){
		return started;
	}
	
}