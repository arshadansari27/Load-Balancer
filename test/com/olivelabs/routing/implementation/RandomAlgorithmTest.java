package com.olivelabs.routing.implementation;

import org.junit.Test;




public class RandomAlgorithmTest extends AlgorithmTest{


	@Test
	public void testGetNodeByAlgorithmWithOutMetrics() throws Exception{
		super.testGetNodeByAlgorithmWithOutMetrics(RoutingAlgorithmFactory.RANDOM_ALGORITHM);
		
	}
	
	@Test
	public void testGetNodeByAlgorithmWithMetrics()  throws Exception{
		super.testGetNodeByAlgorithmWithMetrics(RoutingAlgorithmFactory.RANDOM_ALGORITHM);
	}
	
	@Test
	public void testGetNodeByAlgorithmWithOutNodes() throws Exception{
		super.testGetNodeByAlgorithmWithOutNodes(RoutingAlgorithmFactory.RANDOM_ALGORITHM);
	}
	
	@Test
	public void testAllNodesUsed() throws Exception{
		super.testAllNodesUsed(RoutingAlgorithmFactory.RANDOM_ALGORITHM);
	}

}
