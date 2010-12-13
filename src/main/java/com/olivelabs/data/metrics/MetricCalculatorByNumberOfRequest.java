package com.olivelabs.data.metrics;

import com.olivelabs.data.IMetricCalculator;
import com.olivelabs.data.Metric;

public class MetricCalculatorByNumberOfRequest implements IMetricCalculator{
	
	
	@Override
	public double calculateMetrics(Metric metric) {
		long numberOfRequestServed = metric.getNumberOfRequestServed().longValue();
		long totalRequests = metric.getTotalNumberOfRequestServed().longValue();
		double metrics = (((double)numberOfRequestServed * 1.0)/totalRequests) * 100.0;
		return metrics;
	}
	
}
