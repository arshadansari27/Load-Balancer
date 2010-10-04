package com.olivelabs.data.metrics;


import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.olivelabs.data.IMetricCalculator;

public class MetricCalculatorFactoryTest {

	@Before
	public void setUp() throws Exception {
		MetricCalculatorFactory.init();
	}
	
	@SuppressWarnings("static-access")
	@Test
	public void testGetMetricCalculator()  throws Exception{
		IMetricCalculator metricCalculator = MetricCalculatorFactory.getMetricCalculator(MetricCalculatorFactory.STRATEGY_REQUEST);
		Assert.assertNotNull(metricCalculator);
		Assert.assertTrue(metricCalculator instanceof MetricCalculatorByNumberOfRequest);
		metricCalculator = MetricCalculatorFactory.getMetricCalculator(MetricCalculatorFactory.STRATEGY_REQUEST_SIZE);
		Assert.assertNotNull(metricCalculator);
		Assert.assertTrue(metricCalculator instanceof MetricCalculatorByRequestSize);
		metricCalculator = MetricCalculatorFactory.getMetricCalculator(MetricCalculatorFactory.STRATEGY_DYNAMIC_NODE_INFO);
		Assert.assertNotNull(metricCalculator);
		Assert.assertTrue(metricCalculator instanceof MetricCalculatorByDynamicNodeInfo);
	}

	@After
	public void tearDown() throws Exception {
		MetricCalculatorFactory.destroy();
	}

}
