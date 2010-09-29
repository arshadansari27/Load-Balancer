package com.olivelabs.routing.implementation;

import java.util.Random;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.olivelabs.data.INode;
import com.olivelabs.data.Metric;
import com.olivelabs.data.Node;
import com.olivelabs.queues.NodeQueue;
import com.olivelabs.routing.RoutingAlgorithm;


public class DynamicMetricAlgorithmTest {

	NodeQueue nodes;
	INode node;
	RoutingAlgorithm algorithm;
	int nodeSize = 20;
	int requestCount = 50000;
	
	@Test
	public void testGetNodeByAlgorithmWithOutMetrics() throws Exception{
		nodes = new NodeQueue();
		for(int i=0;i<nodeSize;i++){
			node = new Node("Localhost","909"+i,Metric.getMetric(Metric.STRATEGY_REQUEST_SIZE));
			nodes.addNode(node);
		}
		algorithm = RoutingAlgorithm.getRoutingAlgorithm(RoutingAlgorithm.DYNAMIC,nodes);
		Assert.assertNotNull(algorithm);
		
		for(int i=0;i<requestCount;i++){
			if (nodes.isEmpty() )break;
			node = algorithm.getNodeByAlgorithm();
			Assert.assertNotNull(node);
		}
	}
	
	@Test
	public void testGetNodeByAlgorithmWithMetrics()  throws Exception{
		nodes = new NodeQueue();
		Random r = new Random();
		for(int i=0;i<nodeSize;i++){
			node = new Node("Localhost","909"+i,Metric.getMetric(Metric.STRATEGY_REQUEST_SIZE));
			int value = r.nextInt(1000);
			node.getMetric().setMetrics(Integer.valueOf(value));
			nodes.addNode(node);
		}
		algorithm = RoutingAlgorithm.getRoutingAlgorithm(RoutingAlgorithm.DYNAMIC,nodes);
		Assert.assertNotNull(algorithm);
		
		for(int i=0;i<requestCount;i++){
			if (nodes.isEmpty() )break;
			node = algorithm.getNodeByAlgorithm();
			Assert.assertNotNull(node);
		}
	
	}
	
	@Test
	public void testGetNodeByAlgorithmWithOutNodes() throws Exception{
		nodes = new NodeQueue();
		
		algorithm = RoutingAlgorithm.getRoutingAlgorithm(RoutingAlgorithm.DYNAMIC,nodes);
		Assert.assertNotNull(algorithm);
		//Thread.currentThread().sleep(20000);
		for(int i=0;i<requestCount;i++){
			if (nodes.isEmpty() )break;
			node = algorithm.getNodeByAlgorithm();
			Assert.assertNotNull(node);
		}
	}
}
