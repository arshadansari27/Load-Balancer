package com.olivelabs.data;

public interface IMetric {
	public Long getNumberOfRequestServed();
	public Long getTotalNumberOfRequestServed();
	public Double getRequestServedSizeInMB();
	public Double getTotalRequestServedSizeInMB();
	public void setNumberOfRequestServed(Long servedRequest);
	public void setRequestServedSizeInMB(Double servedRequestSizeInMB);
	public double getMetrics();
	public void resetTotalMetrics();
}
