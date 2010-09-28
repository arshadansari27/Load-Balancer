package com.olivelabs.routing;

import com.olivelabs.data.Node;
import com.olivelabs.queues.NodeQueue;
import com.olivelabs.routing.implementation.DynamicMetricAlgorithm;
import com.olivelabs.routing.implementation.RoundRobinAlgorithm;

public abstract class RoutingAlgorithm {
	public static final String ROUND_ROBIN = "com.olivelabs.routing.implementation.RoundRobinAlgorithm";
	public static final String DYNAMIC = "com.olivelabs.routing.implementation.DynamicMetricAlgorithm";
	
	public static RoutingAlgorithm getRoutingAlgorithm(String algorithm) throws Exception{
		if(ROUND_ROBIN.equals(algorithm)){
			return (RoutingAlgorithm) Class.forName(ROUND_ROBIN).newInstance();
		}
		else if (DYNAMIC.equals(algorithm)){
			return (RoutingAlgorithm) Class.forName(DYNAMIC).newInstance();
		}
		else
			throw new Exception("Routing Strategy not defined!!!");
	}
	
	public abstract Node getNodeByAlgorithm(NodeQueue queue);
}
