package com.olivelabs.data;


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class MetricTest {

static Metric metric;
static IMetricCalculator metricCalculator;
	
	@BeforeClass
	public static void setUp(){	
		metric = new Metric();
		metric.setMetricCalculator(metricCalculator);
		metric.resetTotalMetrics();
		metric.setNumberOfRequestServed(Long.valueOf(10000L));
		metric.setRequestServedSizeInMB(1000000.0D);
	}
	
	
	@Test
	public void testNumberOfRequestMetric() {
		Assert.assertEquals(Long.valueOf(10000), metric.getNumberOfRequestServed());
		Assert.assertEquals(Long.valueOf(10000),metric.getTotalNumberOfRequestServed());
	}
	
	@Test
	public void testRequestServedSizeInMB(){
		Assert.assertEquals(Double.valueOf(1000000.0D),metric.getRequestServedSizeInMB(),0.00);
		Assert.assertEquals(Double.valueOf(1000000.0D),metric.getTotalRequestServedSizeInMB(),0.00);
	}

	@Test(expected=RuntimeException.class)
	public void testGetMetric() throws Exception {
		metric.getMetrics();
	}
}
