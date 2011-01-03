package com.olivelabs.run;


import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;


import com.olivelabs.console.ApplicationConsole;
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
			
			
			PropertyReader reader = new PropertyReader("/home/kenshin/workspace/Load-Balancer/src/main/resource/system.properties");
			HashMap<String, String> properties = reader.getConfiguration();
			
			System.out.println("Loading the load balancer... \nplease wait");
			
			int lbPort = Integer.parseInt((String) properties.get("lb.port"));
			System.out.println("Port Selected ["+lbPort+"]");
			String lbHost = (String) properties.get("lb.host");
			
			String filePath = (String) properties.get("file.path");
			FileReader fileReader = new FileReader(filePath);
	
			IBalancer balancer = new Balancer();
			String routingAlgorithm = (String) properties.get("routing.algorithm");
			System.out.println("Routing Algorithm : "+routingAlgorithm);
			balancer.setAlgorithmName(routingAlgorithm);
			String metricStrategy =(String) properties.get("metric.strategy");
			System.out.println("Metric Strategy : "+metricStrategy);
			balancer.setMetricType(metricStrategy);
	
			
			HashMap<String, ServerAddress> serverAddress = fileReader.getNodesConfiguration();
			Set<String> serverList = serverAddress.keySet();
			for(String key : serverList){
				ServerAddress address = serverAddress.get(key);
				balancer.addNode(address.host,address.port);
				System.out.println("Node added: "+address.host+"["+address.port+"]");
			}
			
			int pool = Integer.parseInt((String) properties.get("lb.pool"));
			Server server = new Server(lbPort,pool);
			server.setBalancer(balancer);
			server.startServer();
			
			Thread thread = new Thread(new ApplicationConsole(balancer));
			thread.start();
			thread.join();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   
		   
	}

}
