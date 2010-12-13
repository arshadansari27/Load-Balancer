package com.olivelabs.routing.implementation;

import com.olivelabs.data.INode;
import com.olivelabs.data.Node;
import com.olivelabs.queues.NodeQueue;
import com.olivelabs.routing.IRouter;


public class RoundRobinAlgorithm implements IRouter{

	@Override
	public INode getNodeByAlgorithm(NodeQueue nodeQueue) {
		if(nodeQueue.isEmpty()) return null;
		INode node;
		synchronized(this)
			{
				node = nodeQueue.getNode(0);
			}
		nodeQueue.removeNode(node);
		nodeQueue.addNode(node,nodeQueue.getSize());
		return node;
	}

}
