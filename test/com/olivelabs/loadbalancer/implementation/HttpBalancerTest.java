package com.olivelabs.loadbalancer.implementation;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.olivelabs.data.Metric;
import com.olivelabs.data.Node;
import com.olivelabs.routing.RoutingAlgorithm;

public class HttpBalancerTest {
	
	HttpBalancer balancer;
	
	@Before
	public void setUp() throws Exception{
		balancer = new HttpBalancer();
		balancer.setAlgorithmName(RoutingAlgorithm.ROUND_ROBIN);
		balancer.setMetricType(Metric.STRATEGY_REQUEST_SIZE);
		for(int i=0;i<10;i++){
			balancer.addNode("Localhost","909"+i);
		}
	}

	@Test
	public void testSetAlgorithmName() throws Exception{
		balancer.setAlgorithmName(RoutingAlgorithm.ROUND_ROBIN);
		Assert.assertEquals(RoutingAlgorithm.ROUND_ROBIN, balancer.getAlgorithmName());
	}

	@Test
	public void testGetNode() {
		Node node = balancer.getNode();
		Assert.assertNotNull(node);
		System.out.println(node.getId());
		
	}

	@Test
	public void testAddNode() throws Exception {
		Node node = balancer.addNode("Localhost","9099");
		Assert.assertNotNull(node);
		System.out.println(node.getId());
		
	}

	@Test
	public void testRemoveNode() throws Exception {
		Node node = balancer.addNode("Localhost","9099");
		Assert.assertNotNull(node);
		System.out.println(node.getId());
		Assert.assertTrue(balancer.removeNode(node));
	}

	@After
	public void tearDown(){
		balancer = null;
	}
}
