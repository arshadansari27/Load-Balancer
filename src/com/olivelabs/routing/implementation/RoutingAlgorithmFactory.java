package com.olivelabs.routing.implementation;


import java.util.ArrayList;
import java.util.List;

import com.olivelabs.data.IMetricCalculator;
import com.olivelabs.routing.IRouter;

public class RoutingAlgorithmFactory {
	public static List<String> listClasses;
	public static List<IRouter> listInstances;
	public static final String ROUND_ROBIN_ALGORITHM = "com.olivelabs.routing.implementation.RoundRobinAlgorithm";
	public static final String DYNAMIC_ALGORITHM = "com.olivelabs.routing.implementation.DynamicMetricAlgorithm";
	public static final String RANDOM_ALGORITHM = "com.olivelabs.routing.implementation.RandomAlgorithm";
	
	static {
		listClasses = new ArrayList<String>();
		listClasses.add(ROUND_ROBIN_ALGORITHM);
		listClasses.add(DYNAMIC_ALGORITHM);
		listClasses.add(RANDOM_ALGORITHM);
	}
	public static IRouter getRoutingAlgorithm(String algorithm) throws Exception{
		if(ROUND_ROBIN_ALGORITHM.equals(algorithm)){
			return (IRouter) Class.forName(ROUND_ROBIN_ALGORITHM).newInstance();
		}
		else if (DYNAMIC_ALGORITHM.equals(algorithm)){
			return (IRouter) Class.forName(DYNAMIC_ALGORITHM).newInstance();
		}
		else if (RANDOM_ALGORITHM.equals(algorithm)){
			return (IRouter) Class.forName(RANDOM_ALGORITHM).newInstance();
		}
		else
			throw new Exception("Routing Strategy not defined!!!");
	}
	
	public static boolean hasRoutingAlgorithm(String algorithm){
		if(listClasses.contains(algorithm)) return true;
		return false;
	}
}
