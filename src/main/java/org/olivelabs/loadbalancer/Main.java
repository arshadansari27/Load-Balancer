package org.olivelabs.loadbalancer;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Main {

	public static void main(String[] args) throws Exception{
		//Executor executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()+2);
		ExecutorService executor = Executors.newCachedThreadPool();
		ConcurrentHashMap<Integer, RequestClass> requestClass = new ConcurrentHashMap<Integer, RequestClass>();
		PriorityBlockingQueue<Request> queue = new PriorityBlockingQueue<Request>();
		PriorityBlockingQueue<Node> nodes = new PriorityBlockingQueue<Node>();
		nodes.add(new Node("www.yahoo.co.in", 80));
		Server server = new Server(9999,executor,queue,requestClass);
		RequestListener listener = new RequestListener(executor, queue, requestClass, nodes);
		executor.execute(listener);
		executor.execute(server);
		
		
		executor.awaitTermination(1, TimeUnit.HOURS);
		
		
		System.out.println("**Request Class");
		for(RequestClass reqClass : requestClass.values())
			System.out.println(reqClass.URL + "=>"+ reqClass.requestProbability +", "+ reqClass.averageServiceTime+", "+reqClass.getClassLevel()+", "+reqClass.SUM_THIS_REQUEST+", "+reqClass.TOTAL_REQUEST_RECEIVED);
		
		System.out.println("** NODES");
		for(Node node : nodes){
			System.out.println(node.hostName +"=>"+node.weight);
			
		System.out.println("** Done");
		}
	}
}
