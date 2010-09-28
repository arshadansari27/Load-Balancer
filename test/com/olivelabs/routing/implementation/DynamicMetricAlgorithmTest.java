package com.olivelabs.routing.implementation;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.olivelabs.data.Metric;
import com.olivelabs.data.Node;
import com.olivelabs.queues.NodeQueue;
import com.olivelabs.routing.RoutingAlgorithm;


public class DynamicMetricAlgorithmTest {

	NodeQueue nodes;
	Node node;
	RoutingAlgorithm algorithm;
	@Before
	public void setUp() throws Exception{
		nodes = new NodeQueue();
		for(int i=0;i<10;i++){
			node = new Node("Localhost","909"+i,Metric.getMetric(Metric.STRATEGY_REQUEST_SIZE));
			int value = (int)(Math.random() * 1000);
			node.getMetric().setMetrics(new Long(value));
			nodes.addNode(node);
			System.out.println(node.getId());
		}
		algorithm = RoutingAlgorithm.getRoutingAlgorithm(RoutingAlgorithm.DYNAMIC);
		Assert.assertNotNull(algorithm);
	}
	@Test
	public void testGetNodeByAlgorithm() {
		
		
		for(int i=0;i<20;i++){
			if (nodes.isEmpty() )break;
			node = algorithm.getNodeByAlgorithm(nodes);
			nodes.removeNode(node);
			System.out.println(node.getId()+".."+node.getPort()+"..."+node.getMetric().getMetrics());
			
		}
	
	}
}
