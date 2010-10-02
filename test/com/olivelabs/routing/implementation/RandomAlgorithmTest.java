package com.olivelabs.routing.implementation;


import java.util.HashMap;
import java.util.Random;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.olivelabs.data.INode;
import com.olivelabs.data.Metric;
import com.olivelabs.data.Node;
import com.olivelabs.queues.NodeQueue;
import com.olivelabs.routing.RoutingAlgorithm;

public class RandomAlgorithmTest {


	NodeQueue nodes;
	INode node;
	RoutingAlgorithm algorithm;
	int nodeSize = 20;
	int requestCount = 300000;
	
	@Ignore
	@Test
	public void testGetNodeByAlgorithmWithOutMetrics() throws Exception{
		nodes = new NodeQueue();
		Random r = new Random();
		for(int i=0;i<nodeSize;i++){
			node = new Node("Localhost","909"+i,Metric.getMetric(Metric.STRATEGY_REQUEST_SIZE));
			int value = r.nextInt(1000);
			//node.getMetric().setMetrics(new Long(value));
			nodes.addNode(node);
			Assert.assertTrue(nodes.hasNode(node));
		}
		algorithm = RoutingAlgorithm.getRoutingAlgorithm(RoutingAlgorithm.RANDOM,nodes);
		Assert.assertNotNull(algorithm);
		
		for(int i=0;i<requestCount;i++){
			if (nodes.isEmpty() )break;
			node = algorithm.getNodeByAlgorithm();
			Assert.assertNotNull(node);
		}
	}
	
	@Ignore
	@Test
	public void testGetNodeByAlgorithmWithMetrics()  throws Exception{
		nodes = new NodeQueue();
		Random r = new Random();
		for(int i=0;i<nodeSize;i++){
			node = new Node("Localhost","909"+i,Metric.getMetric(Metric.STRATEGY_REQUEST_SIZE));
			int value = r.nextInt(1000);
			node.getMetric().setMetrics(Integer.valueOf(value));
			nodes.addNode(node);
			Assert.assertTrue(nodes.hasNode(node));
		}
		algorithm = RoutingAlgorithm.getRoutingAlgorithm(RoutingAlgorithm.RANDOM,nodes);
		Assert.assertNotNull(algorithm);
		
		for(int i=0;i<requestCount;i++){
			if (nodes.isEmpty() )break;
			node = algorithm.getNodeByAlgorithm();
			Assert.assertNotNull(node);
		}
	
	}
	
	@Ignore
	@Test
	public void testGetNodeByAlgorithmWithOutNodes() throws Exception{
		nodes = new NodeQueue();
		
		algorithm = RoutingAlgorithm.getRoutingAlgorithm(RoutingAlgorithm.RANDOM,nodes);
		Assert.assertNotNull(algorithm);
		
		for(int i=0;i<requestCount;i++){
			if (nodes.isEmpty() )break;
			node = algorithm.getNodeByAlgorithm();
			Assert.assertNotNull(node);
		}
	}
	
	
	@Test
	public void testAllNodesUsed() throws Exception{
		HashMap<String,Long> nodeMap = new HashMap<String,Long>();
		nodes = new NodeQueue();
		Random r = new Random();
		for(int i=0;i<nodeSize;i++){
			node = new Node("localhost","909"+i,Metric.getMetric(Metric.STRATEGY_REQUEST_SIZE));
			int value = r.nextInt(1000);
			node.getMetric().setMetrics(Integer.valueOf(value));
			nodes.addNode(node);
			nodeMap.put(node.getId()+"", Long.valueOf(0));
			
		}
		
		
		algorithm = RoutingAlgorithm.getRoutingAlgorithm(RoutingAlgorithm.RANDOM,nodes);
		Assert.assertNotNull(algorithm);
		
		for(int i=0;i<requestCount;i++){
			if (nodes.isEmpty() )break;
			node = algorithm.getNodeByAlgorithm();
						
			long count = nodeMap.get(node.getId()+"");
			count++;
			nodeMap.put(node.getId()+"", Long.valueOf(count));
			System.out.println("Node["+node.getId()+"] => "+count);
			Assert.assertNotNull(node);
		}
		if(nodeSize<requestCount){
			Set<String> nodeIdKeys = nodeMap.keySet();
			
			 for(String nodeIDKey : nodeIdKeys){
				 Assert.assertNotSame("Node not serving, although more " +
				 		"requests than the size of node list were sent.", 0 , nodeMap.get(nodeIDKey));
			 }
		}
	}

}
