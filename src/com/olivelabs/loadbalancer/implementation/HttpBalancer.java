package com.olivelabs.loadbalancer.implementation;

import com.olivelabs.data.INode;
import com.olivelabs.data.Metric;
import com.olivelabs.data.Node;
import com.olivelabs.data.metrics.MetricRequest;
import com.olivelabs.loadbalancer.IBalancer;
import com.olivelabs.queues.NodeQueue;
import com.olivelabs.routing.RoutingAlgorithm;

public class HttpBalancer implements IBalancer {

	NodeQueue queue;
	String algorithmName;
	RoutingAlgorithm routingAlgorithm;
	String metricType;

	public HttpBalancer() {
		queue = new NodeQueue();
		//routingAlgorithm = RoutingAlgorithm.getRoutingAlgorithm(algorithmName);
	}

	public String getAlgorithmName() {
		return algorithmName;
	}

	public void setAlgorithmName(String algorithmName) throws Exception {
		this.algorithmName = algorithmName;
		if(!(RoutingAlgorithm.DYNAMIC.equals(this.algorithmName) || RoutingAlgorithm.ROUND_ROBIN.equals(this.algorithmName)))
			throw new Exception("Please set Routing Algorithm name property!!");
		setRoutingAlgorithm(RoutingAlgorithm.getRoutingAlgorithm(algorithmName));
	}

	public RoutingAlgorithm getRoutingAlgorithm() {
		return routingAlgorithm;
	}

	public void setRoutingAlgorithm(RoutingAlgorithm routingAlgorithm) {
		this.routingAlgorithm = routingAlgorithm;
		
	}

	public String getMetricType() {
		return metricType;
	}

	public void setMetricType(String metricType) {
		this.metricType = metricType;
	}

	@Override
	public INode getNode() throws RuntimeException{
		if(this.routingAlgorithm == null){
			throw new RuntimeException("Please set the routing algorithm property");
		}
		INode node = routingAlgorithm.getNodeByAlgorithm(queue);
		return node;
	}
	
	public INode addNode(String host, String port) throws Exception{
		if(!(Metric.STRATEGY_DYNAMIC.equals(metricType)|| Metric.STRATEGY_REQUEST_SIZE.equals(metricType)))
			throw new RuntimeException("Please set the metric type property");
		Metric metric = Metric.getMetric(metricType);
		INode node = new Node(host,port, metric);
		queue.addNode(node);
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
		return queue.removeNode(node);
	}

	@Override
	public boolean removeNodeById(Long id) {
		Node node = (Node) queue.getNodeById(id.intValue());
		return queue.removeNode(node);
	}

	

}
