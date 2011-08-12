package org.olivelabs.loadbalancer;


import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RequestListenerTest {

	ExecutorService executor;
	Server server;
	RequestListener listener;
	@Before
	public void setUp() throws Exception {
		ConcurrentHashMap<Integer, RequestClass> requestClass = new ConcurrentHashMap<Integer, RequestClass>();
		PriorityBlockingQueue<Request> queue = new PriorityBlockingQueue<Request>();
		PriorityBlockingQueue<Node> nodes = new PriorityBlockingQueue<Node>();
		System.out.println("Starting...");
		executor = Executors.newFixedThreadPool(3);
		server = new Server(9999,executor,queue,requestClass);
		
		listener = new RequestListener(executor, queue, requestClass, nodes);
		executor.execute(listener);
		executor.execute(server);
	}

	
	@Test
	public void testRequestListenerTest() throws Exception{
		
		Socket socket[] = new Socket[10];
		for(int i = 0; i < 10; i++){
			socket[i] = WebClientUtil.getConnection("localhost", 9999);
			
			socket[i].getOutputStream().write((new String("GET /path"+i+" HTTP/1.1\nContent-Type: text/html\n\n")).getBytes());
			socket[i].getOutputStream().flush();
			//new RequestHandler(socket, queue, requestClass).run();
		}
		
		
		
		
		for(int i = 0; i < 10; i++){
			socket[i].close();
		}
		Thread.currentThread().sleep(5000);
		
	}
	@After
	public void tearDown() throws Exception {
		server.stop();
		executor.shutdown();
		executor.awaitTermination(10000, TimeUnit.MILLISECONDS);
		
	}

}
