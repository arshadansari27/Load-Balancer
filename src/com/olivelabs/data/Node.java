package com.olivelabs.data;

public class Node {
	
	private Integer id;
	private String host;
	private Long port;
	private Metric metric;
	private static int count=0;
	public Node(String host, String port, Metric metric) {
		this.id = 1000 + count++;
		this.host=host;
		this.port = Long.parseLong(port);
		this.metric = metric;
	}
	public String getHost() {
		return host;
	}
	public Long getPort() {
		return port;
	}
	public Metric getMetric() {
		return metric;
	}
	public void setMetric(Metric metric) {
		this.metric = metric;
	}
	public int hashCode(){
		return id.intValue();
		
	}
	public Integer getId() {
		
		return this.id;
	}
}
