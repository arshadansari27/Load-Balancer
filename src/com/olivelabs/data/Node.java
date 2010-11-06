package com.olivelabs.data;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.olivelabs.loadbalancer.IClient;
import com.olivelabs.loadbalancer.implementation.HttpClient;
import com.olivelabs.loadbalancer.implementation.RspHandler;
import com.olivelabs.util.ByteArrayConvertor;

public class Node implements INode {

	private Integer id;
	private String host;
	private Long port;
	private IMetric metric;
	private static int count = 0;
	private IClient client;
	private HashMap<Object, Object> requestList;

	public Node(String host, String port, Metric metric)
			throws UnknownHostException {
		this.id = 1000 + count++;
		this.host = host;
		this.port = Long.parseLong(port);
		this.metric = metric;
		this.client = new HttpClient(InetAddress.getByName(host),
				this.port.intValue());
		requestList = new HashMap<Object, Object>();
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
	public void sendRequest(byte[] data, RspHandler handler) throws Exception {
		synchronized (requestList) {
			requestList.put(handler, data);
			requestList.notify();
		}
	}

	@Override
	public void run() {
		while (true) {

			synchronized (requestList) {
				if (requestList.isEmpty()) {
					try {

						requestList.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			for (Object handler : requestList.keySet()) {
				try {
					this.client.send((byte[]) requestList.get(handler),
							(RspHandler) handler);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			requestList.clear();

		}
	}

}
