package com.olivelabs.data;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class NodeTest {
	Node _node;
	String _host;
	String _port;
	Metric metric;
	List<Socket> sockets = new ArrayList<Socket>();
	@Before
	public void setUp() throws Exception{
		
		_host = "localhost";
		_port = "9090";
		metric = new Metric();
		_node = new Node(_host,_port, metric,sockets);
	}
	
	@Test
	public void testGetHost(){
		Assert.assertEquals(this._host, _node.getHost());
	}
	@Test
	public void testGetPort(){
		Long port = Long.parseLong(this._port);
		Assert.assertEquals(port, _node.getPort());
	}
	
	@Test
	public void testGetMetric(){
		IMetric metric = _node.getMetric();
		Assert.assertNotNull(metric);
		
	}
}
