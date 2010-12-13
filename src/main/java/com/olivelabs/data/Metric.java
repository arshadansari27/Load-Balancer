package com.olivelabs.data;


public class Metric implements IMetric{
	
	private Double requestServedSizeInMB;
	private static Double totalRequestServedSizeInMB;
	private Long numberOfRequestServed;
	private static Long totalNumberOfRequestServed;
	private IMetricCalculator metricCalculator;
	
	static{
		totalNumberOfRequestServed = 0L;
		totalRequestServedSizeInMB = 0.0D;
	}
	public Metric(){
		
		requestServedSizeInMB = 0.0D;
		numberOfRequestServed = 0L;
	}
	public Metric(IMetricCalculator mCal){
		this.metricCalculator  = mCal;
		requestServedSizeInMB = 0.0D;
		numberOfRequestServed = 0L;
	}
		
	
	public void setMetricCalculator(IMetricCalculator metricCalculator){
		this.metricCalculator = metricCalculator;
	}
	
	public IMetricCalculator getMetricCalculator(){
		return this.metricCalculator;
	}
	
	public double getMetrics() throws RuntimeException{
		if(this.metricCalculator == null) throw new RuntimeException("The Metric Calculator reference is null and was not set");
		return this.metricCalculator.calculateMetrics(this);
	}
	//Incase of MetricRequest : Set will add and incase of Dynamic: it will replace
	

	public Long getNumberOfRequestServed() {
		// TODO Auto-generated method stub
		return this.numberOfRequestServed;
	}

	public Double getRequestServedSizeInMB() {
		// TODO Auto-generated method stub
		return this.requestServedSizeInMB;
	}

	public Long getTotalNumberOfRequestServed() {
		// TODO Auto-generated method stub
		return totalNumberOfRequestServed;
	}

	public Double getTotalRequestServedSizeInMB() {
		// TODO Auto-generated method stub
		return totalRequestServedSizeInMB;
	}

	public void setNumberOfRequestServed(Long servedRequest) {
		long requestServed = this.numberOfRequestServed.longValue() + servedRequest.longValue();
		this.numberOfRequestServed = Long.valueOf(requestServed);
		long totalRequestServed = this.totalNumberOfRequestServed.longValue() + servedRequest.longValue();
		this.totalNumberOfRequestServed = Long.valueOf(totalRequestServed);
		if(numberOfRequestServed.longValue() > totalNumberOfRequestServed.longValue()) totalNumberOfRequestServed =  Long.valueOf(numberOfRequestServed.longValue());
		
	}

	public void setRequestServedSizeInMB(Double servedRequestSizeInMB) {
		double requestSizeServed = this.requestServedSizeInMB.doubleValue() + servedRequestSizeInMB.doubleValue();
		this.requestServedSizeInMB = Double.valueOf(requestSizeServed);
		double totalRequestSizedServed = this.totalRequestServedSizeInMB.doubleValue() + servedRequestSizeInMB.doubleValue();
		this.totalRequestServedSizeInMB = Double.valueOf(totalRequestSizedServed);
		if(requestServedSizeInMB.doubleValue() > totalRequestServedSizeInMB.doubleValue()) totalRequestServedSizeInMB = Double.valueOf(requestServedSizeInMB.doubleValue());
	}
	@Override
	public void resetTotalMetrics() {
		totalNumberOfRequestServed = 0L;
		totalRequestServedSizeInMB = 0.0D;
	}

	
}
