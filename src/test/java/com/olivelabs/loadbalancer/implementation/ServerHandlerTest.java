package com.olivelabs.loadbalancer.implementation;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.olivelabs.data.Request;
import com.olivelabs.data.Ri;


public class ServerHandlerTest {
	Server server;
	ConcurrentLinkedQueue<Request> QCHIGH;
	ConcurrentLinkedQueue<Request> QCMEDIUM;
	ConcurrentLinkedQueue<Request> QCLOW;
	ConcurrentHashMap<Long, Ri> requests;
	
	@Before
	public void setUp() throws Exception{
		server = new Server(9090, 10);
		
		QCHIGH = new ConcurrentLinkedQueue<Request>();
		QCMEDIUM = new ConcurrentLinkedQueue<Request>();
		QCLOW = new ConcurrentLinkedQueue<Request>();
		requests = new ConcurrentHashMap<Long, Ri>();
		
		server.setQCj(QCHIGH, QCMEDIUM, QCLOW);
		server.setRequestsTree(requests);
		server.startServer();
				
	}
	
	
	@SuppressWarnings("static-access")
	@Test
	public void testServerHandler() throws UnknownHostException, IOException, InterruptedException{
		Thread.currentThread().sleep(5000);
		for (int j = 0; j < 100; j++) {
			Socket testSocket = new Socket("localhost", 9090);
			PrintWriter out = new PrintWriter(testSocket.getOutputStream());
			out.write("GET /"+j%10+" HTTP/1.0\r\n\r\n");
			out.flush();
			
		}
		
		
		
		Thread.currentThread().sleep(5000);
		
	

		System.out.println("QC High ["+QCHIGH.size()+"]");
		/*for(Request request : QCHIGH){
			System.out.println((request.getHashCode() + "::"+request.URL+ "::"+new String(request.buffer)));
		}*/
		System.out.println("QC Medium ["+QCMEDIUM.size()+"]");
		/*for(Request request : QCMEDIUM){
			System.out.println((request.getHashCode() + "::"+request.URL+ "::"+new String(request.buffer)));
		}*/
		System.out.println("QC Low ["+QCLOW.size()+"]");
		/*for(Request request : QCLOW){
			System.out.println((request.getHashCode() + "::"+request.URL+ "::"+new String(request.buffer)));
		}*/
		
		System.out.println("Ri Tree... ["+requests.size()+"]");
		Enumeration<Long> keys = requests.keys();
		while(keys.hasMoreElements()){
			Long key = keys.nextElement();
			Ri ri = requests.get(key);
			System.out.println("**********************************************************");
			System.out.println(ri.hashCode+"\tR="+ri.requestCount+"\tT="+ri.totalRequestCount+"\tP="+ri.getProbability()+"\t"+ri.getPriority());
		}

	}
	
	@After
	public void tearDown(){
		QCHIGH	 		= 	null;
		QCMEDIUM 		= 	null;
		QCLOW 			= 	null;
		requests 		= 	null;
	}

}
