package com.olivelabs.routing.implementation;

import static org.junit.Assert.*;


import org.junit.Before;
import org.junit.Test;

import com.olivelabs.data.metrics.MetricCalculatorFactory;


public class DynamicMetricAlgorithmTest extends AlgorithmTest{

	
	
	@Test
	public void testGetNodeByAlgorithmWithOutMetrics() throws Exception{
		metricCalculator= MetricCalculatorFactory.getMetricCalculator(MetricCalculatorFactory.STRATEGY_REQUEST);
		super.testGetNodeByAlgorithmWithOutMetrics(RoutingAlgorithmFactory.DYNAMIC_ALGORITHM);
		metricCalculator= MetricCalculatorFactory.getMetricCalculator(MetricCalculatorFactory.STRATEGY_REQUEST_SIZE);
		super.testGetNodeByAlgorithmWithOutMetrics(RoutingAlgorithmFactory.DYNAMIC_ALGORITHM);
		metricCalculator= MetricCalculatorFactory.getMetricCalculator(MetricCalculatorFactory.STRATEGY_DYNAMIC_NODE_INFO);
		super.testGetNodeByAlgorithmWithOutMetrics(RoutingAlgorithmFactory.DYNAMIC_ALGORITHM);
		
	}
	
	@Test
	public void testGetNodeByAlgorithmWithMetrics()  throws Exception{
		metricCalculator= MetricCalculatorFactory.getMetricCalculator(MetricCalculatorFactory.STRATEGY_REQUEST);
		super.testGetNodeByAlgorithmWithMetrics(RoutingAlgorithmFactory.DYNAMIC_ALGORITHM);
		metricCalculator= MetricCalculatorFactory.getMetricCalculator(MetricCalculatorFactory.STRATEGY_REQUEST_SIZE);
		super.testGetNodeByAlgorithmWithMetrics(RoutingAlgorithmFactory.DYNAMIC_ALGORITHM);
		metricCalculator= MetricCalculatorFactory.getMetricCalculator(MetricCalculatorFactory.STRATEGY_DYNAMIC_NODE_INFO);
		super.testGetNodeByAlgorithmWithMetrics(RoutingAlgorithmFactory.DYNAMIC_ALGORITHM);
	}
	
	@Test
	public void testGetNodeByAlgorithmWithOutNodes() throws Exception{
		metricCalculator= MetricCalculatorFactory.getMetricCalculator(MetricCalculatorFactory.STRATEGY_REQUEST);
		super.testGetNodeByAlgorithmWithOutNodes(RoutingAlgorithmFactory.DYNAMIC_ALGORITHM);
		metricCalculator= MetricCalculatorFactory.getMetricCalculator(MetricCalculatorFactory.STRATEGY_REQUEST_SIZE);
		super.testGetNodeByAlgorithmWithMetrics(RoutingAlgorithmFactory.DYNAMIC_ALGORITHM);
		metricCalculator= MetricCalculatorFactory.getMetricCalculator(MetricCalculatorFactory.STRATEGY_DYNAMIC_NODE_INFO);
		super.testGetNodeByAlgorithmWithMetrics(RoutingAlgorithmFactory.DYNAMIC_ALGORITHM);
	}
	
	@Test
	public void testAllNodesUsed() throws Exception{
		metricCalculator= MetricCalculatorFactory.getMetricCalculator(MetricCalculatorFactory.STRATEGY_REQUEST);
		super.testAllNodesUsed(RoutingAlgorithmFactory.DYNAMIC_ALGORITHM);
		metricCalculator= MetricCalculatorFactory.getMetricCalculator(MetricCalculatorFactory.STRATEGY_REQUEST_SIZE);
		super.testGetNodeByAlgorithmWithMetrics(RoutingAlgorithmFactory.DYNAMIC_ALGORITHM);
		metricCalculator= MetricCalculatorFactory.getMetricCalculator(MetricCalculatorFactory.STRATEGY_DYNAMIC_NODE_INFO);
		super.testGetNodeByAlgorithmWithMetrics(RoutingAlgorithmFactory.DYNAMIC_ALGORITHM);
	}
}
