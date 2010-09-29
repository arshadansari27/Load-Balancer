package com.olivelabs.routing.implementation;

import java.util.concurrent.Executor;

import com.olivelabs.data.INode;
import com.olivelabs.routing.RoutingAlgorithm;

public class DynamicMetricAlgorithm extends RoutingAlgorithm {

	INode node;
	NodeSetter nodeSetter = new NodeSetter(this);
	double highestRequest = 10000D;

	Executor executor = new Executor() {

		@Override
		public void execute(Runnable command) {
			command.run();

		}
	};

	@Override
	public synchronized INode getNodeByAlgorithm() {
		if (node == null)
			getBestNode();
		else
			executor.execute(nodeSetter);
		return node;
	}

	public synchronized void getBestNode() {
		if (nodeQueue.isEmpty())
			return;
		
		Integer id = null;
		double metric;
		double temp = 0.0;
		boolean firstTime = true;
		for (INode n : nodeQueue.getAll()) {
			metric = n.getMetric().getMetrics();
			if(firstTime){
				temp = metric+1;
				firstTime = false;
			}
			if (metric < highestRequest) {
				if (temp > metric) {
					temp = metric;
					id = n.getId();
				}
			}
			else{
				if (highestRequest <= metric)
					getHighestRequest();
			}
		}
		if (id != null) {
			node = nodeQueue.getNodeById(id);
			node.setMetricValue(Integer.valueOf(1));
			//System.out.println("Node[" + node.getId() + "] served ["
				//	+ node.getMetric().getMetrics() + "] requests..");
		} else
			throw new RuntimeException("Dynamic Algorithm didn't get any node!");
	}

	private void getHighestRequest() {
		double temp = 0;
		double metric = 0;
		for (INode n : nodeQueue.getAll()) {
			metric = n.getMetric().getMetrics();
			if (metric > temp) {
				temp = metric;
			}
		}
		highestRequest =  Double.valueOf(temp+1.0);
	}

	private class NodeSetter implements Runnable {

		DynamicMetricAlgorithm algorithm;

		public NodeSetter(DynamicMetricAlgorithm algorithm) {
			this.algorithm = algorithm;
		}

		@Override
		public void run() {
			algorithm.getBestNode();
		}

	}
}
