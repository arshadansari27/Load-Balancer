package com.olivelabs.routing.implementation;


import java.util.HashMap;
import java.util.Random;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import com.olivelabs.data.IMetricCalculator;
import com.olivelabs.data.INode;
import com.olivelabs.data.Metric;
import com.olivelabs.data.Node;
import com.olivelabs.data.metrics.MetricCalculatorFactory;
import com.olivelabs.queues.NodeQueue;
import com.olivelabs.routing.IRouter;

public abstract class AlgorithmTest {

	NodeQueue nodes;
	INode node;
	IRouter algorithm;
	int nodeSize = 20;
	int requestCount = 50000;
	IMetricCalculator metricCalculator;
	Metric metric;
	
	@Before
	public void setUp() throws Exception{
		metricCalculator= MetricCalculatorFactory.getMetricCalculator(MetricCalculatorFactory.STRATEGY_REQUEST);
		metric = new Metric(metricCalculator);
		metric.setNumberOfRequestServed(Long.valueOf(10000L));
		metric.setRequestServedSizeInMB(Double.valueOf(10000.0D));
		
	}
	
	
	
	public void testGetNodeByAlgorithmWithOutMetrics(String routingAlgorithm) throws Exception{
		
		nodes = new NodeQueue();
		for(int i=0;i<nodeSize;i++){
			node = new Node("Localhost","909"+i,metric);
			nodes.addNode(node);
		}
		algorithm = RoutingAlgorithmFactory.getRoutingAlgorithm(routingAlgorithm);
		Assert.assertNotNull(algorithm);
		
		for(int i=0;i<requestCount;i++){
			if (nodes.isEmpty() )break;
			node = algorithm.getNodeByAlgorithm(nodes);
			Assert.assertNotNull(node);
		}
	}
	public void testGetNodeByAlgorithmWithMetrics(String routingAlgorithm)  throws Exception{
		nodes = new NodeQueue();
		Random r = new Random();
		for(int i=0;i<nodeSize;i++){
			node = new Node("Localhost","909"+i,metric);
			metric.setNumberOfRequestServed(Long.valueOf(r.nextInt(1000)));
			metric.setRequestServedSizeInMB(Double.valueOf(r.nextDouble()*10000));
			node.setMetric(metric);
			nodes.addNode(node);
		}
		algorithm = RoutingAlgorithmFactory.getRoutingAlgorithm(routingAlgorithm);
		
		Assert.assertNotNull(algorithm);
		for(int i=0;i<requestCount;i++){
			if (nodes.isEmpty() )break;
			node = algorithm.getNodeByAlgorithm(nodes);
			
			Assert.assertNotNull(node);
		}
	
	}
	
	public void testGetNodeByAlgorithmWithOutNodes(String routingAlgorithm)  throws Exception{
		nodes = new NodeQueue();
		algorithm = RoutingAlgorithmFactory.getRoutingAlgorithm(routingAlgorithm);
		Assert.assertNotNull(algorithm);
		//Thread.currentThread().sleep(20000);
		for(int i=0;i<requestCount;i++){
			if (nodes.isEmpty() )break;
			node = algorithm.getNodeByAlgorithm(nodes);
			Assert.assertNotNull(node);
		}
	}
	
	public void testAllNodesUsed(String routingAlgorithm)  throws Exception{
		HashMap<String,Long> nodeMap = new HashMap<String,Long>();
		nodes = new NodeQueue();
		Random r = new Random();
		for(int i=0;i<nodeSize;i++){
			node = new Node("localhost","909"+i,metric);
			metric.setNumberOfRequestServed(Long.valueOf(r.nextInt(1000)));
			metric.setRequestServedSizeInMB(Double.valueOf(r.nextDouble()*10000));
			node.setMetric(metric);
			
			nodes.addNode(node);
			nodeMap.put(node.getId()+"", Long.valueOf(0));
			
		}
		
		
		algorithm = RoutingAlgorithmFactory.getRoutingAlgorithm(routingAlgorithm);
		Assert.assertNotNull(algorithm);
		
		for(int i=0;i<requestCount;i++){
			if (nodes.isEmpty() )break;
			node = algorithm.getNodeByAlgorithm(nodes);
						
			long count = nodeMap.get(node.getId()+"");
			count++;
			nodeMap.put(node.getId()+"", Long.valueOf(count));
			//System.out.println("Node["+node.getId()+"] => "+count);
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
