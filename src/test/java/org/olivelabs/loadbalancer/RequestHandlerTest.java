package org.olivelabs.loadbalancer;

import java.io.IOException;
import java.net.Socket;
import java.sql.Time;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.junit.Test;


public class RequestHandlerTest {

	@Test
	public void testRequestHandler() throws Exception{
		ConcurrentHashMap<Integer, RequestClass> requestClass = new ConcurrentHashMap<Integer, RequestClass>();
		PriorityBlockingQueue<Request> queue = new PriorityBlockingQueue<Request>();
		System.out.println("Starting...");
		ExecutorService executor = Executors.newFixedThreadPool(2);
		
		for(int i = 0; i < 10; i++){
			Socket socket = WebClientUtil.getServerSocket();
			executor.execute(new RequestHandler(socket, queue, requestClass));
			//new RequestHandler(socket, queue, requestClass).run();
		}
		System.out.println("Done...");
		System.out.println("Printing...");
		executor.shutdown();
		executor.awaitTermination(10000, TimeUnit.MILLISECONDS);
		for(Integer i : requestClass.keySet()){
			RequestClass reqClass = requestClass.get(i);
			System.out.println(i+"==>"+reqClass.URL+", "+reqClass.getClassLevel()+", "+reqClass.TOTAL_REQUEST_RECEIVED);
		}
		
		for(Request request : queue){
			System.out.println(request.URL+"=>"+request.REQUEST_TEXT);
		}
		System.out.println("DOne...");
	}
}



