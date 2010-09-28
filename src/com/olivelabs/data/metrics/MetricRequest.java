package com.olivelabs.data.metrics;

import com.olivelabs.data.Metric;

public class MetricRequest extends Metric{
	private static Long totalRequests;
	
	private Long numberOfRequestServed;
	
	static{
		totalRequests = 0L;
	}
	public MetricRequest(){
		super();
		numberOfRequestServed = 0L;
	}
	public Double getMetrics(){
		return ((double)numberOfRequestServed/totalRequests) * 100.0;
	}
	public void setMetrics(Object value){
		Long temp;
		if(value instanceof Long){
			temp = (Long) value;
			numberOfRequestServed = numberOfRequestServed.longValue() + temp.longValue();
			totalRequests = totalRequests.longValue() + temp.longValue();
		}
		else{
			numberOfRequestServed++;
			totalRequests++;
		}
	}
}
