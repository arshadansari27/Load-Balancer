package com.olivelabs.data.metrics;

import java.util.ArrayList;
import java.util.List;

import org.xsocket.IDestroyable;

import com.olivelabs.data.IMetricCalculator;

public class MetricCalculatorFactory  {
	public static List<String> listClasses;
	public static List<IMetricCalculator> listInstances;
	public static final String STRATEGY_REQUEST = "com.olivelabs.data.metrics.MetricCalculatorByNumberOfRequest";
	public static final String STRATEGY_REQUEST_SIZE = "com.olivelabs.data.metrics.MetricCalculatorByRequestSize";
	public static final String STRATEGY_DYNAMIC_NODE_INFO = "com.olivelabs.data.metrics.MetricCalculatorByDynamicNodeInfo";
	
	static {
		listClasses = new ArrayList<String>();
		listClasses.add(STRATEGY_REQUEST);
		listClasses.add(STRATEGY_REQUEST_SIZE);
		listClasses.add(STRATEGY_DYNAMIC_NODE_INFO);
	}
	public static IMetricCalculator getMetricCalculator(String strategy) throws Exception{
		init();
		if (listClasses.contains(strategy)){
			IMetricCalculator metricCalculator = (IMetricCalculator) Class.forName(strategy).newInstance();
			listInstances.add(metricCalculator);
			return metricCalculator;
		}
		else
			throw new Exception("Strategy not defined!!!");
	}
	
	public static void init(){
		if(listInstances==null) listInstances = new ArrayList<IMetricCalculator>(); 
	}
	
	public static void destroy(){
		if(listInstances!=null) {
			listInstances.clear();
			listInstances = null;
		}
	}
}
