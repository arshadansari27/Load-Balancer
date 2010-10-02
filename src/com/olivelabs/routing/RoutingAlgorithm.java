package com.olivelabs.routing;

import com.olivelabs.data.INode;
import com.olivelabs.data.Node;
import com.olivelabs.queues.NodeQueue;
import com.olivelabs.routing.implementation.DynamicMetricAlgorithm;
import com.olivelabs.routing.implementation.RoundRobinAlgorithm;

public abstract class RoutingAlgorithm {
	public static final String ROUND_ROBIN = "com.olivelabs.routing.implementation.RoundRobinAlgorithm";
	public static final String DYNAMIC = "com.olivelabs.routing.implementation.DynamicMetricAlgorithm";
	public static final String RANDOM = "com.olivelabs.routing.implementation.RandomAlgorithm";
	protected static NodeQueue nodeQueue;
	public static RoutingAlgorithm getRoutingAlgorithm(String algorithm, NodeQueue queue) throws Exception{
		nodeQueue =  queue;
		if(ROUND_ROBIN.equals(algorithm)){
			return (RoutingAlgorithm) Class.forName(ROUND_ROBIN).newInstance();
		}
		else if (DYNAMIC.equals(algorithm)){
			return (RoutingAlgorithm) Class.forName(DYNAMIC).newInstance();
		}
		else if (RANDOM.equals(algorithm)){
			return (RoutingAlgorithm) Class.forName(RANDOM).newInstance();
		}
		else
			throw new Exception("Routing Strategy not defined!!!");
	}
	
	public abstract INode getNodeByAlgorithm();
}
