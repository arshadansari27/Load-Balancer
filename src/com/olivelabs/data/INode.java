package com.olivelabs.data;

public interface INode {

	public String getHost();
	public Long getPort();
	public IMetric getMetric();
	public void setMetric(IMetric metric);
	public Integer getId();
	public void setRequestServedSizeInMB(Double value);
	void setNumberOfRequestServed(Long value);
}
