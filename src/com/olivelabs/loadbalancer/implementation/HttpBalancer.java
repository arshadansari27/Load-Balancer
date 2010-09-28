package com.olivelabs.loadbalancer.implementation;

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
		if(!(this.algorithmName == RoutingAlgorithm.DYNAMIC || this.algorithmName==RoutingAlgorithm.ROUND_ROBIN))
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
	public Node getNode() throws RuntimeException{
		if(this.routingAlgorithm == null){
			throw new RuntimeException("Please set the routing algorithm property");
		}
		Node node = routingAlgorithm.getNodeByAlgorithm(queue);
		return node;
	}
	
	public Node addNode(String host, String port) throws Exception{
		if(!(Metric.STRATEGY_DYNAMIC.equals(metricType)|| Metric.STRATEGY_REQUEST_SIZE.equals(metricType)))
			throw new RuntimeException("Please set the metric type property");
		Metric metric = Metric.getMetric(metricType);
		Node node = new Node(host,port, metric);
		queue.addNode(node);
		return node;
		
	}

	@Override
	public boolean isNodeQueueEmpty() {
		return queue.isEmpty();
	}

	@Override
	public boolean isNodeUp(Node node) {
		return queue.hasNode(node);
	}

	@Override
	public boolean removeNode(Node node) {
		return queue.removeNode(node);
	}

	@Override
	public boolean removeNodeById(Long id) {
		Node node = queue.getNodeById(id.intValue());
		return queue.removeNode(node);
	}

	

}
