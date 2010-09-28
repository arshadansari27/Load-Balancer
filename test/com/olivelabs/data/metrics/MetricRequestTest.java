package com.olivelabs.data.metrics;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.olivelabs.data.Metric;

public class MetricRequestTest {

	Metric metric;
	
	
	@Before
	public void setUp(){	
		metric = new MetricRequest();
		metric.setMetrics(new Long(1000));
		metric.setMetrics(new Long(1000));
		metric.setMetrics(new Long(1000));
		metric.setMetrics(new Long(1000));
	}
	
	
	@Test
	public void testGetMetrics() {
		System.out.println(metric.getMetrics());
	}


	@Test
	public void testGetMetric() throws Exception {
		Metric metricObj = Metric.getMetric(Metric.STRATEGY_REQUEST_SIZE);
		Assert.assertNotNull(metricObj);
		//metricObj = Metric.getMetric(Metric.STRATEGY_DYNAMIC);
		//Assert.assertNull(metricObj);
	}

}
