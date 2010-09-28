package com.olivelabs.routing.implementation;

import com.olivelabs.data.Node;
import com.olivelabs.queues.NodeQueue;
import com.olivelabs.routing.RoutingAlgorithm;

public class DynamicMetricAlgorithm extends RoutingAlgorithm {

	@Override
	public Node getNodeByAlgorithm(NodeQueue queue) {
		if(queue.isEmpty()) return null;
		Integer id=-1;
		Double metric;
		Double temp=10000000.00D;
		for(Node n : queue.getAll()){
			metric = n.getMetric().getMetrics();
			if (metric < temp) {
				temp = metric;
				id = n.getId();
			}
		}
		if(id == -1) throw new RuntimeException("Error occured!");
		return queue.getNodeById(id);
	}

}
