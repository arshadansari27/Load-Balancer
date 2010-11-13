package com.olivelabs.queues;

import static org.junit.Assert.*;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.olivelabs.data.INode;
import com.olivelabs.data.Metric;
import com.olivelabs.data.Node;
import com.olivelabs.data.metrics.MetricCalculatorByNumberOfRequest;

public class NodeQueueTest {

	NodeQueue nodes;
	INode node;
	List<Socket> sockets = new ArrayList<Socket>();
	@Before
	public void setUp() throws Exception{
		nodes = new NodeQueue();
		
		nodes.addNode(new Node("","2342",new Metric(new MetricCalculatorByNumberOfRequest()), sockets));
		nodes.addNode(new Node("","2342",new Metric(new MetricCalculatorByNumberOfRequest()), sockets));
		nodes.addNode(new Node("","2342",new Metric(new MetricCalculatorByNumberOfRequest()), sockets));
		nodes.addNode(new Node("","2342",new Metric(new MetricCalculatorByNumberOfRequest()), sockets));
		nodes.addNode(new Node("","2342",new Metric(new MetricCalculatorByNumberOfRequest()), sockets));
		nodes.addNode(new Node("","2342",new Metric(new MetricCalculatorByNumberOfRequest()), sockets));
		node = new Node("Localhost","9090",new Metric(new MetricCalculatorByNumberOfRequest()), sockets);
		nodes.addNode(node);
	}
	
	@Test
	public void testAddNode() throws Exception{
		INode node1 = new Node("localhost","2342",new Metric(new MetricCalculatorByNumberOfRequest()), sockets);
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
