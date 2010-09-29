package com.olivelabs.data;

import java.util.ArrayList;
import java.util.List;

public abstract class Metric {
	
	public static final String STRATEGY_REQUEST_SIZE = "com.olivelabs.data.metrics.MetricRequest";
	public static final String STRATEGY_DYNAMIC = "";
	public static List<String> metrics;
	
	static {
		metrics = new ArrayList<String>();
		metrics.add(STRATEGY_REQUEST_SIZE);
		metrics.add(STRATEGY_DYNAMIC);
	}
	public static Metric getMetric(String strategy) throws Exception{
		if (metrics.contains(strategy)){
		return (Metric) Class.forName(strategy).newInstance();
		}
		else
			throw new Exception("Strategy not defined!!!");
	}
	public abstract double getMetrics();
	//Incase of MetricRequest : Set will add and incase of Dynamic: it will replace
	public abstract void setMetrics(Object value);
}
