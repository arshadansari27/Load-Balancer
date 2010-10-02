package com.olivelabs.routing.implementation;

import java.util.Random;

import com.olivelabs.data.INode;
import com.olivelabs.routing.RoutingAlgorithm;

public class RandomAlgorithm extends RoutingAlgorithm {

	@Override
	public INode getNodeByAlgorithm() {
		if(nodeQueue.isEmpty()) return null;
		INode node;
		Random random = new Random();
		int index = random.nextInt(nodeQueue.getSize());
		synchronized(this)
			{
				node = nodeQueue.getNode(index%nodeQueue.getSize());
			}
		
		node.setMetricValue(Integer.valueOf(1));
		
		return node;
	}

}
