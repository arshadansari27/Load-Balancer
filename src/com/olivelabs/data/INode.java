package com.olivelabs.data;

public interface INode {

	public String getHost();
	public Long getPort();
	public Metric getMetric();
	public void setMetric(Metric metric);
	public Integer getId();
}
