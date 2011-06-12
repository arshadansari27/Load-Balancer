package com.olivelabs.loadbalancer.implementation;

import static org.junit.Assert.*;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.olivelabs.data.IMetric;
import com.olivelabs.data.Metric;
import com.olivelabs.data.Request;
import com.olivelabs.data.metrics.MetricCalculatorByNumberOfRequest;

public class RequestURLExtractorTest {

	public ServerSocket server;
	public Socket socket;
	WorkerThread2 worker;

	@Before
	public void setUp() throws Exception {
		server = new ServerSocket(8989);
		worker = new WorkerThread2(server);
		new Thread(worker).start();
		Thread.currentThread().sleep(1000);
	}


	
	@Test
	public void testGetURL() throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException {
		Socket testSocket ;
		for (int i = 0; i < 1; i++) {
			testSocket = new Socket("localhost", 8989);
			PrintWriter out = new PrintWriter(testSocket.getOutputStream());
			out.write("GET / HTTP/1.0\r\n\r\n");
			out.flush();
		}
		while(true){
		socket = worker.getSocket();
		Request request = RequestURLExtractor.getURL(socket);
		System.out.println(request.URL);
		Thread.currentThread().sleep(30000);
		}
		
	}

	@After
	public void tearDown() throws IOException{
		socket.close();
		server.close();
		
	}
}
