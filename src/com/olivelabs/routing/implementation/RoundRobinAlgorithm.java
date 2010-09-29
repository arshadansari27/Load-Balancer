package com.olivelabs.routing.implementation;

import com.olivelabs.data.INode;
import com.olivelabs.data.Node;
import com.olivelabs.queues.NodeQueue;
import com.olivelabs.routing.RoutingAlgorithm;


public class RoundRobinAlgorithm extends RoutingAlgorithm {

	@Override
	public INode getNodeByAlgorithm(NodeQueue queue) {
		if(queue.isEmpty()) return null;
		INode node = queue.getNode(0);
		queue.removeNode(node);
		queue.addNode(node,queue.getSize());
		return node;
	}

}
