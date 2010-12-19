package com.olivelabs.run;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;


import com.olivelabs.loadbalancer.IBalancer;
import com.olivelabs.loadbalancer.IClient;
import com.olivelabs.loadbalancer.IServer;
import com.olivelabs.loadbalancer.implementation.Balancer;
import com.olivelabs.loadbalancer.implementation.Client;
import com.olivelabs.loadbalancer.implementation.Server;


public class MainClass {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Properties props = new Properties();
	        /*InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream("system.properties");
	        
	        if (inputStream == null) {
	            throw new FileNotFoundException("property file 'system.properties' not found in the classpath");
	        }

	        props.load(inputStream);*/
			
			props.put("routing.algorithm", "com.olivelabs.routing.implementation.RoundRobinAlgorithm");
			props.put("metric.strategy", "com.olivelabs.data.metrics.MetricCalculatorByNumberOfRequest");
			props.put("lb.host", "localhost");
			props.put("lb.port", "9009");
			System.out.println("Loading the load balancer... \nplease wait");
			
			int lbPort = Integer.parseInt((String) props.get("lb.port"));
			System.out.println("Port Selected ["+lbPort+"]");
			String lbHost = (String) props.get("lb.host");
			String routingAlgorithm = (String) props.get("routing.algorithm");
			String metricStrategy =(String) props.get("metric.strategy");
			IBalancer balancer = new Balancer();
			System.out.println("Routing Algorithm : "+routingAlgorithm);
			balancer.setAlgorithmName(routingAlgorithm);
			System.out.println("Metric Strategy : "+metricStrategy);
			balancer.setMetricType(metricStrategy);
			
			//balancer.addNode("www.google.com","80");
			//balancer.addNode("kenshin-server","8080");
			balancer.addNode("www.finicity.com","80");
			balancer.addNode("www.finicity.com","80");
			balancer.addNode("www.finicity.com","80");

			Server server = new Server(lbPort,1);
			server.setBalancer(balancer);
			server.startServer();
			
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   
		   
	}

}
