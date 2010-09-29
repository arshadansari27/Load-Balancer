package com.olivelabs.queues;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.olivelabs.data.INode;
import com.olivelabs.data.Metric;
import com.olivelabs.data.Node;

public class NodeQueueTest {

	NodeQueue nodes;
	INode node;
	@Before
	public void setUp() throws Exception{
		nodes = new NodeQueue();
		nodes.addNode(new Node("","2342",Metric.getMetric(Metric.STRATEGY_REQUEST_SIZE)));
		nodes.addNode(new Node("","2342",Metric.getMetric(Metric.STRATEGY_REQUEST_SIZE)));
		nodes.addNode(new Node("","2342",Metric.getMetric(Metric.STRATEGY_REQUEST_SIZE)));
		nodes.addNode(new Node("","2342",Metric.getMetric(Metric.STRATEGY_REQUEST_SIZE)));
		nodes.addNode(new Node("","2342",Metric.getMetric(Metric.STRATEGY_REQUEST_SIZE)));
		nodes.addNode(new Node("","2342",Metric.getMetric(Metric.STRATEGY_REQUEST_SIZE)));
		node = new Node("Localhost","9090",Metric.getMetric(Metric.STRATEGY_REQUEST_SIZE));
		nodes.addNode(node);
	}
	
	@Test
	public void testAddNode() throws Exception{
		INode node1 = new Node("WTF","2342",Metric.getMetric(Metric.STRATEGY_REQUEST_SIZE));
		Integer id = node1.getId();
		nodes.addNode(node1);
		INode node2 = nodes.getNodeById(id);
		Assert.assertEquals(node1, node2);
		
	}

	@Test
	public void testRemoveNode() {
		nodes.removeNode(node);
		Assert.assertFalse(nodes.hasNode(node));
	}

	@Test
	public void testGetNode() {
		INode node = nodes.getNode(0);
		Assert.assertNotNull(node);
		
	}


}
