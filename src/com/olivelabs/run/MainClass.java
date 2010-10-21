package com.olivelabs.run;


import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.Properties;


import com.olivelabs.loadbalancer.IBalancer;
import com.olivelabs.loadbalancer.IClient;
import com.olivelabs.loadbalancer.IServerHandler;
import com.olivelabs.loadbalancer.IServer;
import com.olivelabs.loadbalancer.implementation.HttpBalancer;
import com.olivelabs.loadbalancer.implementation.HttpClient;
import com.olivelabs.loadbalancer.implementation.HttpServer;
import com.olivelabs.loadbalancer.implementation.HttpServerHandler;


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
			//RequestHandlerChain chain = new RequestHandlerChain();
			IBalancer balancer = new HttpBalancer();
			//
			//balancer.setAlgorithmName(RoutingAlgorithm.ROUND_ROBIN);
			System.out.println("Routing Algorithm : "+routingAlgorithm);
			balancer.setAlgorithmName(routingAlgorithm);
			//balancer.setMetricType(Metric.STRATEGY_REQUEST_SIZE);
			System.out.println("Metric Strategy : "+metricStrategy);
			balancer.setMetricType(metricStrategy);
			balancer.addNode("www.google.com","80");
			balancer.addNode("www.finicity.com","80");
			balancer.addNode("www.thedyinggod.com","80");
			//balancer.addNode("localhost","8090");
			
			//HttpRequestHandler requestHandler = new HttpRequestHandler(balancer);
			//chain.addLast(requestHandler);

			//HttpServer proxy =  new HttpServer(lbPort, chain);
			//proxy.setAutoCompressThresholdBytes(Integer.MAX_VALUE);
			//proxy.setAutoUncompress(false);
			
			//proxy.run();
			
			IServer server = new HttpServer("localhost", 9090);
			IClient client = new HttpClient(balancer);
			IServerHandler requestHandler = new HttpServerHandler(client);
			server.setRequestHandler(requestHandler);
			server.start();
			System.out.println("Load balancer is running!");
			//proxy.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   
		   
	}

}
