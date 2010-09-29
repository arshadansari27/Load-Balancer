package com.olivelabs.routing.implementation;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.olivelabs.data.INode;
import com.olivelabs.data.Metric;
import com.olivelabs.data.Node;
import com.olivelabs.queues.NodeQueue;
import com.olivelabs.routing.RoutingAlgorithm;

public class RoundRobinAlgorithmTest {


	NodeQueue nodes;
	INode node;
	RoutingAlgorithm algorithm;
	
	@Test
	public void testGetNodeByAlgorithmWithOutMetrics() throws Exception{
		nodes = new NodeQueue();
		Random r = new Random();
		for(int i=0;i<10;i++){
			node = new Node("Localhost","909"+i,Metric.getMetric(Metric.STRATEGY_REQUEST_SIZE));
			int value = r.nextInt(1000);
			//node.getMetric().setMetrics(new Long(value));
			nodes.addNode(node);
		}
		algorithm = RoutingAlgorithm.getRoutingAlgorithm(RoutingAlgorithm.ROUND_ROBIN);
		Assert.assertNotNull(algorithm);
		
		for(int i=0;i<20;i++){
			if (nodes.isEmpty() )break;
			node = algorithm.getNodeByAlgorithm(nodes);
			nodes.removeNode(node);			
		}
	}
	
	
	@Test
	public void testGetNodeByAlgorithmWithMetrics()  throws Exception{
		nodes = new NodeQueue();
		Random r = new Random();
		for(int i=0;i<10;i++){
			node = new Node("Localhost","909"+i,Metric.getMetric(Metric.STRATEGY_REQUEST_SIZE));
			int value = r.nextInt(1000);
			node.getMetric().setMetrics(Integer.valueOf(value));
			nodes.addNode(node);
		}
		algorithm = RoutingAlgorithm.getRoutingAlgorithm(RoutingAlgorithm.ROUND_ROBIN);
		Assert.assertNotNull(algorithm);
		
		for(int i=0;i<20;i++){
			if (nodes.isEmpty() )break;
			node = algorithm.getNodeByAlgorithm(nodes);
			nodes.removeNode(node);
			
		}
	
	}
	
	
	@Test
	public void testGetNodeByAlgorithmWithOutNodes() throws Exception{
		nodes = new NodeQueue();
		
		algorithm = RoutingAlgorithm.getRoutingAlgorithm(RoutingAlgorithm.ROUND_ROBIN);
		Assert.assertNotNull(algorithm);
		
		for(int i=0;i<20;i++){
			if (nodes.isEmpty() )break;
			node = algorithm.getNodeByAlgorithm(nodes);
			nodes.removeNode(node);		
		}
	}

}
