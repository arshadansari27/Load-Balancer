package com.olivelabs.console;

import java.awt.List;
import java.io.Console;
import java.io.IOException;

import com.olivelabs.data.INode;
import com.olivelabs.loadbalancer.IBalancer;

public class ApplicationConsole implements Runnable {

	private IBalancer balancer;

	public ApplicationConsole(IBalancer balancer) {
		this.balancer = balancer;
	}

	public void run() {
		System.out.println("Load Balancer....");
		while (true) {
			try {
				Thread.currentThread().sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				Runtime.getRuntime().exec("clear");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (balancer.isNodeQueueEmpty()) {
				System.out
						.println("Please add nodes through the configuration file and then restart the load balancer..");
			} else {
				System.out
						.println("Node\tRequestServed\tRequestSize\tUtilization");
				java.util.List<INode> nodes;
				synchronized (balancer.getNodes()) {
					nodes = balancer.getNodes().getAll();
				}
				for (INode node : nodes) {
					long numberOfRequestServed;
					long totalNumberOfRequestServed;
					double numberOfRequestSizeServed ;
					double totalNumberOfRequestSizeServed ;
					synchronized (node) {
						numberOfRequestServed = node.getMetric()
								.getNumberOfRequestServed();
						totalNumberOfRequestServed = node.getMetric()
								.getTotalNumberOfRequestServed();
						numberOfRequestSizeServed = node.getMetric()
								.getRequestServedSizeInMB();
						totalNumberOfRequestSizeServed= node
								.getMetric().getTotalRequestServedSizeInMB();
					}
					double requestNumberUtilization = (double) numberOfRequestServed
							/ totalNumberOfRequestServed;
					double requestSizeUtilization = numberOfRequestSizeServed
							/ totalNumberOfRequestSizeServed;
					double utilization = ((requestNumberUtilization + requestSizeUtilization) / 2) * 100;
					System.out.println(node.getId() + "\t\t"
							+ numberOfRequestServed + "\t\t"
							+ numberOfRequestSizeServed + "\t\t" + utilization);
				}
			}

		}
	}

}
