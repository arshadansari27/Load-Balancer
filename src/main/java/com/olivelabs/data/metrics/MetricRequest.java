package com.olivelabs.data.metrics;

import com.olivelabs.data.Metric;

public class MetricRequest extends Metric{
	private static Long totalRequests = 0L;
	
	private Long numberOfRequestServed;
	

	public MetricRequest(){
		super();
		numberOfRequestServed = 0L;
	}
	public double getMetrics(){
		double metrics = (((double)numberOfRequestServed * 1.0)/totalRequests) * 100.0;
		if(Double.isNaN(metrics)) return 0.0;
		return metrics; 
	}
	public void setMetrics(Object value){
		int temp = 1;
		if(value instanceof Long){	
			temp = ((Long) value).intValue();
		}
		if(value instanceof Integer){	
			temp = ((Integer) value).intValue();
		}
		numberOfRequestServed = Long.valueOf(numberOfRequestServed.longValue() + temp);
		totalRequests = Long.valueOf(totalRequests.longValue() + temp);
		
		
	}
}
