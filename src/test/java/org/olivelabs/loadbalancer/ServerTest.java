package org.olivelabs.loadbalancer;


import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ServerTest {

	@Before
	public void setUp() throws Exception {
	}

	@SuppressWarnings("static-access")
	@Test
	public void testRequestHandler() throws Exception{
		ConcurrentHashMap<Integer, RequestClass> requestClass = new ConcurrentHashMap<Integer, RequestClass>();
		PriorityBlockingQueue<Request> queue = new PriorityBlockingQueue<Request>();
		System.out.println("Starting...");
		ExecutorService executor = Executors.newFixedThreadPool(2);
		Server server = new Server(9999,executor,queue,requestClass);
		executor.execute(server);
		Socket socket[] = new Socket[10];
		for(int i = 0; i < 10; i++){
			socket[i] = WebClientUtil.getConnection("localhost", 9999);
			
			socket[i].getOutputStream().write((new String("GET /path"+i+" HTTP/1.1\nContent-Type: text/html\n\n")).getBytes());
			socket[i].getOutputStream().flush();
			//new RequestHandler(socket, queue, requestClass).run();
		}
		
		System.out.println("Done...");
		System.out.println("Printing...");
		for(int i = 0; i < 10; i++){
			socket[i].close();
		}
		for(Integer i : requestClass.keySet()){
			RequestClass reqClass = requestClass.get(i);
			System.out.println(i+"==>"+reqClass.URL+", "+reqClass.getClassLevel()+", "+reqClass.TOTAL_REQUEST_RECEIVED);
		}
		
		for(Request request : queue){
			System.out.println(request.URL+"=>"+request.REQUEST_TEXT);
		}
		System.out.println("DOne...");
		server.stop();
		executor.shutdown();
		executor.awaitTermination(10000, TimeUnit.MILLISECONDS);
	}
	
	@After
	public void tearDown() throws Exception {
	}

}
