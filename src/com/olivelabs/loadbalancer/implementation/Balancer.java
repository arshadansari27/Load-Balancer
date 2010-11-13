package com.olivelabs.loadbalancer.implementation;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.olivelabs.data.IMetric;
import com.olivelabs.data.IMetricCalculator;
import com.olivelabs.data.INode;
import com.olivelabs.data.Metric;
import com.olivelabs.data.Node;
import com.olivelabs.data.metrics.MetricCalculatorFactory;
import com.olivelabs.loadbalancer.IBalancer;
import com.olivelabs.queues.NodeQueue;
import com.olivelabs.routing.IRouter;
import com.olivelabs.routing.implementation.RoutingAlgorithmFactory;

public class Balancer implements IBalancer {

	private NodeQueue queue;
	private List<Socket> socketsQueue;
	private NodeQueue haltedQueue;
	private String algorithmName;
	private IRouter router;
	private String metricType;
	private IMetricCalculator metricCalculator;
	private Executor executor;
	public Balancer() {
		queue = new NodeQueue();
		socketsQueue = new ArrayList<Socket>();
		executor = Executors.newCachedThreadPool();
	}

	public String getAlgorithmName() {
		return algorithmName;
	}

	public void setAlgorithmName(String algorithmName) throws Exception {
		this.algorithmName = algorithmName;
		if(!(RoutingAlgorithmFactory.hasRoutingAlgorithm(algorithmName)))
			throw new Exception("Please set Routing Algorithm name property!!");
		router = RoutingAlgorithmFactory.getRoutingAlgorithm(algorithmName);
	}


	public String getMetricType() {
		return metricType;
	}

	public void setMetricType(String metricType) throws Exception {
		this.metricType = metricType;
		IMetricCalculator newMetricCalculator = MetricCalculatorFactory.getMetricCalculator(metricType);
		setMetricCalculator(newMetricCalculator);
	}

	public IMetricCalculator getMetricCalculator() {
		return metricCalculator;
	}

	public void setMetricCalculator(IMetricCalculator metricCalculator) {
		this.metricCalculator = metricCalculator;
	}

	@Override
	public INode getNode() throws RuntimeException{
		if(this.router == null){
			throw new RuntimeException("Please set the routing algorithm property");
		}
		INode node = router.getNodeByAlgorithm(this.queue);
		return node;
	}
	
	public INode addNode(String host, String port) throws Exception{
		Metric metric = new Metric();
		metric.setMetricCalculator(metricCalculator);
		INode node = new Node(host,port, metric, socketsQueue);
		queue.addNode(node);
		executor.execute(node);
		return node;
		
	}

	@Override
	public boolean isNodeQueueEmpty() {
		return queue.isEmpty();
	}

	@Override
	public boolean isNodeUp(INode node) {
		return queue.hasNode(node);
	
	}

	@Override
	public boolean removeNode(INode node) {
		if(node.stop())
			return queue.removeNode(node);
		else
			return false;
	}

	@Override
	public boolean removeNodeById(Long id) {
		INode node = (Node) queue.getNodeById(id.intValue());
		if(node.stop())
			return queue.removeNode(node);
		else
			return false;
	}

	
	
	public boolean upNode(INode node){
		if(haltedQueue.hasNode(node) && !queue.hasNode(node)){
			queue.addNode(node);
			haltedQueue.removeNode(node);
			node.start();
			executor.execute(node);
			return true;
		}
		else
			return false;
	}
	
	public boolean downNode(INode node){
		if(queue.hasNode(node) && !haltedQueue.hasNode(node)){
			queue.removeNode(node);
			haltedQueue.addNode(node);
			node.stop();
			return true;
		}
		else
			return false;
	}
	
	public boolean reload(INode node){
		if(queue.hasNode(node) && !haltedQueue.hasNode(node)){
			node.stop();
			node.start();
			executor.execute(node);
			return true;
		}
		else
			return false;
	}

	@Override
	public void handle(Socket socket) {
		// TODO Auto-generated method stub
		
	}
}
