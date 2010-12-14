package com.olivelabs.loadbalancer.implementation;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.olivelabs.data.IMetric;
import com.olivelabs.data.Metric;
import com.olivelabs.data.metrics.MetricCalculatorByNumberOfRequest;

public class ClientTest {

	public static Client client;
	public ServerSocket server;
	public Socket socket;

	@Before
	public void setUp() throws Exception {
		client = new Client(InetAddress.getByName("www.finicity.com"), 80);
		IMetric metric = new Metric(new MetricCalculatorByNumberOfRequest());
		client.setMetrics(metric);
		server = new ServerSocket(8989);
		new Thread(new WorkerThread2(server, client)).start();
	}

	@Test
	public void testHandleRequest() throws Exception {
		int length = 1024;
		for (int i = 0; i < 1; i++) {
			Socket testSocket = new Socket("localhost", 8989);
			PrintWriter out = new PrintWriter(testSocket.getOutputStream());
			BufferedInputStream in = new BufferedInputStream(
					testSocket.getInputStream());
			
			
			out.println("GET / HTTP/1.0\n\n");
			out.flush();
			byte[] response = new byte[length];
			int read = 0;
			ArrayList bytes = new ArrayList();
			while ((read = in.read(response)) != -1) {
				bytes.add(response);
				for(int i1=0;i1<response.length;i1++){
					Assert.assertTrue(Character.isDefined((char) response[i1] ));
				}
				if(read < length){
					break;
				}
			}
			int completeLenght = bytes.size()*length;
			byte[] finalBytes = new byte[completeLenght]; 
				int currStart=0;
			int count=1;
			for(Object o : bytes){
				byte[] buff = (byte[]) o;
				System.arraycopy(buff, 0, finalBytes, currStart, length);
				currStart = length * count++;
			}
			
			
			
			Assert.assertNotNull(finalBytes);
			Assert.assertTrue(finalBytes.length >= 1);
			
		}
	}

	@After
	public void tearDown(){
		try {
			Thread.currentThread().sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
class WorkerThread2 implements Runnable{
	ServerSocket server;
	Client client;
	public WorkerThread2(ServerSocket server, Client client){
		this.server = server;
		this.client = client;
	}
	public void run(){
		
			try {
				Socket socket = server.accept();
				
				client.handleRequest(socket);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}
	
}
