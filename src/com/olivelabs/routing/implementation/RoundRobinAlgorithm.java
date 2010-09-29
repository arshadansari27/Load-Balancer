package com.olivelabs.routing.implementation;

import com.olivelabs.data.INode;
import com.olivelabs.data.Node;
import com.olivelabs.queues.NodeQueue;
import com.olivelabs.routing.RoutingAlgorithm;


public class RoundRobinAlgorithm extends RoutingAlgorithm {

	@Override
	public INode getNodeByAlgorithm() {
		if(nodeQueue.isEmpty()) return null;
		INode node;
		synchronized(this)
			{
				node = nodeQueue.getNode(0);
			}
		nodeQueue.removeNode(node);
		node.setMetricValue(Integer.valueOf(1));
		nodeQueue.addNode(node,nodeQueue.getSize());
		return node;
	}

}
