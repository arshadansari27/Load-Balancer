package com.olivelabs.data.metrics;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.olivelabs.data.IMetricCalculator;
import com.olivelabs.data.Metric;

public class MetricCalculatorByRequestSizeTest {

	IMetricCalculator metricCalculator =  new MetricCalculatorByRequestSize();
	Metric metric;
	
	@Before
	public void setUp(){	
		metric = new Metric();
		metric.setMetricCalculator(metricCalculator);
		metric.resetTotalMetrics();
		metric.setNumberOfRequestServed(Long.valueOf(10000L));
		metric.setRequestServedSizeInMB(Double.valueOf(10000.0D));
		
	}
	

	@Test
	public void testCalculateMetrics() throws Exception {
		Assert.assertEquals(100.0D, metricCalculator.calculateMetrics(metric),0.00);
	}
	

}
