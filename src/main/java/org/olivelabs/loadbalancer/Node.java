package org.olivelabs.loadbalancer;

public class Node implements Comparable<Node> {
	
	public String hostName;
	public int port;
	public Double weight;
	public long requestServerdByThisNode;

	public Node(String hostName, int port){
		this.hostName = hostName;
		this.port = port;
		this.weight = Double.MAX_VALUE;
		requestServerdByThisNode=0L;
	}
	
	public int compareTo(Node o) {
		if(this.weight < o.weight) return -1;
		else if(this.weight > o.weight) return 1;
		else return 0;
	}
	
	public void updateNodeWithNewRequest(){
		requestServerdByThisNode++;
		weight = Double.valueOf(requestServerdByThisNode * 1.0  / RequestClass.TOTAL_REQUEST_RECEIVED);
	}
}
