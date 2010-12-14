package com.olivelabs.data;



import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class NodeTest {
	Node _node;
	String _host;
	String _port;
	Metric metric;
	@Before
	public void setUp() throws Exception{
		
		_host = "localhost";
		_port = "9090";
		metric = new Metric();
		_node = new Node(_host,_port, metric);
	}
	
	@Test
	public void testNodeStartStop(){
		Assert.assertTrue(_node.start());
		Assert.assertTrue(_node.stop());
	}
	@Test
	public void testNodeMetric(){
		Assert.assertNotNull(_node.getMetric());
	}
}
