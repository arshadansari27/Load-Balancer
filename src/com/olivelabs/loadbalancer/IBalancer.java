package com.olivelabs.loadbalancer;

import com.olivelabs.data.Node;
import com.olivelabs.routing.RoutingAlgorithm;

public interface IBalancer {

	
	//Make sure that you update the metrics of Node before returning it
	Node getNode() throws Exception;
	Node addNode(String host, String port) throws Exception;
	
	boolean removeNode(Node node);
	
	boolean removeNodeById(Long id);
	
	boolean isNodeUp(Node n);
	
	boolean isNodeQueueEmpty();
	
	public String getAlgorithmName();

	public void setAlgorithmName(String algorithmName) throws Exception ;

	public RoutingAlgorithm getRoutingAlgorithm() ;
	
	public void setRoutingAlgorithm(RoutingAlgorithm routingAlgorithm);

	public String getMetricType();

	public void setMetricType(String metricType);

	
}
