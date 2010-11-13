package com.olivelabs.loadbalancer;

import java.net.Socket;
import java.nio.channels.SocketChannel;

import com.olivelabs.data.INode;
import com.olivelabs.data.Node;

public interface IBalancer {

	
	//Make sure that you update the metrics of Node before returning it
	INode getNode() throws Exception;
	INode addNode(String host, String port) throws Exception;
	
	boolean removeNode(INode node);
	
	boolean removeNodeById(Long id);
	
	boolean isNodeUp(INode n);
	
	boolean isNodeQueueEmpty();
	
	public String getAlgorithmName();

	public void setAlgorithmName(String algorithmName) throws Exception ;

	
	public String getMetricType();

	public void setMetricType(String metricType) throws Exception;
	void handle(Socket socket);

	
}
