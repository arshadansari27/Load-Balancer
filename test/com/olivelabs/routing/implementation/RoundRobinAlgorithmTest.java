package com.olivelabs.routing.implementation;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.olivelabs.data.Metric;
import com.olivelabs.data.Node;
import com.olivelabs.queues.NodeQueue;
import com.olivelabs.routing.RoutingAlgorithm;

public class RoundRobinAlgorithmTest {

	int nodesSize;
	NodeQueue nodes;
	Node node;
	RoutingAlgorithm algorithm;
	@Before
	public void setUp() throws Exception{
		nodesSize = 10;
		nodes = new NodeQueue();
		for(int i=0;i<nodesSize;i++){
			node = new Node("Localhost","909"+i,Metric.getMetric(Metric.STRATEGY_REQUEST_SIZE));
			int value = (int)(Math.random() * 1000);
			node.getMetric().setMetrics(new Long(value));
			nodes.addNode(node);
			System.out.println(node.getId());
		}
		algorithm = RoutingAlgorithm.getRoutingAlgorithm(RoutingAlgorithm.ROUND_ROBIN);
		Assert.assertNotNull(algorithm);
	}
	@Test
	public void testGetNodeByAlgorithm() {
		
		
		for(int i=0;i<20;i++){
			node = algorithm.getNodeByAlgorithm(nodes);
			System.out.println(node.getId()+".."+node.getPort()+"..."+node.getMetric().getMetrics());
			
		}
		Assert.assertEquals(nodesSize,nodes.getSize());
	}

}
