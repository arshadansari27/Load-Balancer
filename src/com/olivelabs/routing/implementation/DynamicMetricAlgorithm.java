package com.olivelabs.routing.implementation;

import com.olivelabs.data.INode;
import com.olivelabs.data.Node;
import com.olivelabs.queues.NodeQueue;
import com.olivelabs.routing.RoutingAlgorithm;

public class DynamicMetricAlgorithm extends RoutingAlgorithm {

	@Override
	public INode getNodeByAlgorithm(NodeQueue queue) {
		if(queue.isEmpty()) return null;
		Integer id=null;
		double metric;
		double temp=10000000.00D;
		for(INode n : queue.getAll()){
			metric = n.getMetric().getMetrics();
			if (metric < temp) {
				temp = metric;
				id = n.getId();
			}
		}
		if(id!= null) {
			INode node = queue.getNodeById(id);
			node.getMetric().setMetrics(Integer.valueOf(1));
			return node;
		}
		else throw new RuntimeException("Dynamic Algorithm didn't get any node!");
	}

}
