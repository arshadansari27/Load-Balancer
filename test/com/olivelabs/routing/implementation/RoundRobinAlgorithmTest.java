package com.olivelabs.routing.implementation;


import org.junit.Test;

public class RoundRobinAlgorithmTest extends AlgorithmTest{


	@Test
	public void testGetNodeByAlgorithmWithOutMetrics() throws Exception{
		super.testGetNodeByAlgorithmWithOutMetrics(RoutingAlgorithmFactory.ROUND_ROBIN_ALGORITHM);
		
	}
	
	@Test
	public void testGetNodeByAlgorithmWithMetrics()  throws Exception{
		super.testGetNodeByAlgorithmWithMetrics(RoutingAlgorithmFactory.ROUND_ROBIN_ALGORITHM);
	}
	
	@Test
	public void testGetNodeByAlgorithmWithOutNodes() throws Exception{
		super.testGetNodeByAlgorithmWithOutNodes(RoutingAlgorithmFactory.ROUND_ROBIN_ALGORITHM);
	}
	
	@Test
	public void testAllNodesUsed() throws Exception{
		super.testAllNodesUsed(RoutingAlgorithmFactory.ROUND_ROBIN_ALGORITHM);
	}

}
