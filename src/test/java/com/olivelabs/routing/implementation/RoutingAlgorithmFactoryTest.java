package com.olivelabs.routing.implementation;


import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.olivelabs.routing.IRouter;

public class RoutingAlgorithmFactoryTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGetRoutingAlgorithm() throws Exception{
		IRouter router;
		router = RoutingAlgorithmFactory.getRoutingAlgorithm(RoutingAlgorithmFactory.ROUND_ROBIN_ALGORITHM);
		Assert.assertNotNull(router);
		Assert.assertTrue(router instanceof RoundRobinAlgorithm);
		router = RoutingAlgorithmFactory.getRoutingAlgorithm(RoutingAlgorithmFactory.RANDOM_ALGORITHM);
		Assert.assertNotNull(router);
		Assert.assertTrue(router instanceof RandomAlgorithm);
		router = RoutingAlgorithmFactory.getRoutingAlgorithm(RoutingAlgorithmFactory.DYNAMIC_ALGORITHM);
		Assert.assertNotNull(router);
		Assert.assertTrue(router instanceof DynamicMetricAlgorithm);
	}
	
	@Test
	public void testHasRoutingAlgorithm() throws Exception{
		Assert.assertTrue(RoutingAlgorithmFactory.hasRoutingAlgorithm(RoutingAlgorithmFactory.ROUND_ROBIN_ALGORITHM));
		Assert.assertTrue(RoutingAlgorithmFactory.hasRoutingAlgorithm(RoutingAlgorithmFactory.RANDOM_ALGORITHM));
		Assert.assertTrue(RoutingAlgorithmFactory.hasRoutingAlgorithm(RoutingAlgorithmFactory.DYNAMIC_ALGORITHM));
	}
	
	@After
	public void tearDown() throws Exception {
	}

}
