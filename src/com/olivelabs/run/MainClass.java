package com.olivelabs.run;


import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

import org.xlightweb.RequestHandlerChain;
import org.xlightweb.server.HttpServer;

import com.olivelabs.loadbalancer.IBalancer;
import com.olivelabs.loadbalancer.implementation.HttpBalancer;
import com.olivelabs.loadbalancer.server.HttpRequestHandler;


public class MainClass {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.setProperty("org.xlightweb.showDetailedError", "true");

		
		try {
			Properties props = new Properties();
	        InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream("system.properties");

	        if (inputStream == null) {
	            throw new FileNotFoundException("property file 'system.properties' not found in the classpath");
	        }

	        props.load(inputStream);
			
			System.out.println("Loading the load balancer... \nplease wait");
			
			int lbPort = Integer.parseInt((String) props.get("lb.port"));
			System.out.println("Port Selected ["+lbPort+"]");
			String lbHost = (String) props.get("lb.host");
			String routingAlgorithm = (String) props.get("routing.algorithm");
			String metricStrategy =(String) props.get("metric.strategy");
			RequestHandlerChain chain = new RequestHandlerChain();
			IBalancer balancer = new HttpBalancer();
			//
			//balancer.setAlgorithmName(RoutingAlgorithm.ROUND_ROBIN);
			System.out.println("Routing Algorithm : "+routingAlgorithm);
			balancer.setAlgorithmName(routingAlgorithm);
			//balancer.setMetricType(Metric.STRATEGY_REQUEST_SIZE);
			System.out.println("Metric Strategy : "+metricStrategy);
			balancer.setMetricType(metricStrategy);
			balancer.addNode("localhost","80");
			//balancer.addNode("localhost","8090");
			
			HttpRequestHandler requestHandler = new HttpRequestHandler(balancer);
			chain.addLast(requestHandler);

			HttpServer proxy =  new HttpServer(lbPort, chain);
			proxy.setAutoCompressThresholdBytes(Integer.MAX_VALUE);
			proxy.setAutoUncompress(false);
			
			proxy.run();
			
			System.out.println("Load balancer is running!");
			//proxy.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   
		   
	}

}
