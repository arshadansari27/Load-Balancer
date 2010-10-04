package com.olivelabs.routing.implementation;

import java.util.Random;

import com.olivelabs.data.INode;
import com.olivelabs.queues.NodeQueue;
import com.olivelabs.routing.IRouter;

public class RandomAlgorithm implements IRouter {

	

	@Override
	public INode getNodeByAlgorithm(NodeQueue nodeQueue) {
		if(nodeQueue.isEmpty()) return null;
		INode node;
		Random random = new Random();
		int index = random.nextInt(nodeQueue.getSize());
		synchronized(this)
			{
				node = nodeQueue.getNode(index%nodeQueue.getSize());
			}
		
		
		
		return node;
	}

}
