package com.olivelabs.run;


import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;


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
			props.put("lb.pool", "10");
			props.put("file.path", "/home/kenshin/workspace/Load-Balancer/src/main/servers.txt");
			
			System.out.println("Loading the load balancer... \nplease wait");
			
			int lbPort = Integer.parseInt((String) props.get("lb.port"));
			System.out.println("Port Selected ["+lbPort+"]");
			String lbHost = (String) props.get("lb.host");
			
			String filePath = (String) props.get("file.path");
			FileReader reader = new FileReader(filePath);
	
			IBalancer balancer = new Balancer();
			String routingAlgorithm = (String) props.get("routing.algorithm");
			System.out.println("Routing Algorithm : "+routingAlgorithm);
			balancer.setAlgorithmName(routingAlgorithm);
			String metricStrategy =(String) props.get("metric.strategy");
			System.out.println("Metric Strategy : "+metricStrategy);
			balancer.setMetricType(metricStrategy);
	
			
			HashMap<String, ServerAddress> serverAddress = reader.getNodesConfiguration();
			Set<String> serverList = serverAddress.keySet();
			for(String key : serverList){
				ServerAddress address = serverAddress.get(key);
				balancer.addNode(address.host,address.port);
				System.out.println("Node added: "+address.host+"["+address.port+"]");
			}
			
			int pool = Integer.parseInt((String) props.get("lb.pool"));
			Server server = new Server(lbPort,pool);
			server.setBalancer(balancer);
			server.startServer();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   
		   
	}

}
