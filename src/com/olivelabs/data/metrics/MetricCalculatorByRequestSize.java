package com.olivelabs.data.metrics;

import com.olivelabs.data.IMetricCalculator;
import com.olivelabs.data.Metric;

public class MetricCalculatorByRequestSize implements IMetricCalculator {

	@Override
	public double calculateMetrics(Metric metric) {
		double requestServedSize = metric.getRequestServedSizeInMB().doubleValue();
		double totalRequestServedSize = metric.getTotalRequestServedSizeInMB().doubleValue();
		double temp1 = (requestServedSize/totalRequestServedSize) * 100;
		double numberOfRequestServed = metric.getNumberOfRequestServed().doubleValue();
		double totalNumberOfRequestServed = metric.getTotalNumberOfRequestServed().doubleValue();
		double temp2 = (numberOfRequestServed/totalNumberOfRequestServed)*100;
		return (temp1+temp2)/2;
	}

}
