package com.olivelabs.loadbalancer.implementation;

import static org.junit.Assert.*;

import java.io.BufferedInputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.olivelabs.data.INode;
import com.olivelabs.data.Metric;
import com.olivelabs.data.Node;
import com.olivelabs.data.metrics.MetricCalculatorFactory;
import com.olivelabs.routing.implementation.RoutingAlgorithmFactory;

public class BalancerTest {
	
	Balancer balancer;
	
	@Before
	public void setUp() throws Exception{
		balancer = new Balancer();
		balancer.setAlgorithmName(RoutingAlgorithmFactory.DYNAMIC_ALGORITHM);
		balancer.setMetricType(MetricCalculatorFactory.STRATEGY_REQUEST);
		for(int i=0;i<10;i++){
			balancer.addNode("localhost","1024"+i);
		}
	}

	
	@Test
	public void testSetAlgorithmName() throws Exception{
		balancer.setAlgorithmName(RoutingAlgorithmFactory.ROUND_ROBIN_ALGORITHM);
		Assert.assertEquals(RoutingAlgorithmFactory.ROUND_ROBIN_ALGORITHM, balancer.getAlgorithmName());
		balancer.setAlgorithmName(RoutingAlgorithmFactory.DYNAMIC_ALGORITHM);
	}

	@Ignore
	@Test
	public void testGetNodeDynamically() throws Exception {
		balancer.setAlgorithmName(RoutingAlgorithmFactory.DYNAMIC_ALGORITHM);

		for(int i=0;i<10000;i++){
			if(balancer.isNodeQueueEmpty())
				break;
			INode node = balancer.getNode();
			Assert.assertNotNull(node);
			Assert.assertNotNull(node.getId());
		}
		
		
	}

	@Test
	public void testGetNodeRoundRobin() throws Exception {
		balancer.setAlgorithmName(RoutingAlgorithmFactory.ROUND_ROBIN_ALGORITHM);
		
		for(int i=0;i<10000;i++){
			if(balancer.isNodeQueueEmpty())
				break;
			INode node = balancer.getNode();
			Assert.assertNotNull(node);
			Assert.assertNotNull(node.getId());
		}
		
		
	}
	@Test
	public void testGetNodeRandom() throws Exception {
		balancer.setAlgorithmName(RoutingAlgorithmFactory.RANDOM_ALGORITHM);
		
		for(int i=0;i<10000;i++){
			if(balancer.isNodeQueueEmpty())
				break;
			INode node = balancer.getNode();
			Assert.assertNotNull(node);
			Assert.assertNotNull(node.getId());
		}
		
		
	}

	@Test
	public void testAddNode() throws Exception {
		INode node = balancer.addNode("localhost","10480");
		Assert.assertNotNull(node);
		Assert.assertNotNull(node.getId());
		
	}
	
	@Test
	public void testRemoveNode() throws Exception {
		INode node = balancer.addNode("localhost","10481");
		Assert.assertNotNull(node);
		Assert.assertNotNull(node.getId());
		Assert.assertTrue(balancer.removeNode(node));
	}

	@Ignore
	@Test
	public void testHandle() throws Exception{
		Node _node;
		String _host;
		String _port;
		Metric metric;
		ServerSocket serverSocket ;
		_host = "www.google.com";
		_port = "80";
		metric = new Metric();
		_node = new Node(_host,_port, metric);
		serverSocket = new ServerSocket(10485);
		Executor executor = Executors.newCachedThreadPool(new ThreadFactory() {
			
			@Override
			public Thread newThread(Runnable r) {
				Thread t = new Thread(r);
				return t;
			}
		});
		executor.execute(_node);
int length = 1024;
		
		for(int i=0;i<10;i++){
			Socket socket = new Socket("localhost",10485);
			Socket incoming = serverSocket.accept();
			incoming.setSoTimeout(5000);
			_node.handleRequest(incoming);
			PrintWriter out = new PrintWriter(socket.getOutputStream());
			BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
			out.println("GET / HTTP/1.0\n\n");
			out.flush();
			byte[] response = new byte[length];
			int read = 0;
			while ((read = in.read(response)) != -1) {
				for(int i1=0;i1<read;i1++){
					Assert.assertTrue(Character.isDefined((char) response[i1] ));
				}
			}
			
		}
	}
	@After
	public void tearDown(){
		balancer = null;
		try {
			Thread.currentThread().sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
