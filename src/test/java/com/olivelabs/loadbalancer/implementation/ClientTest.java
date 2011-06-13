package com.olivelabs.loadbalancer.implementation;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

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

			out.write("GET / HTTP/1.0\r\n\r\n");
			out.flush();
			byte[] response = new byte[length];
			int read = 0;
			try{
			while ((read = in.read(response)) != -1) {
				byte[] temp = new byte[read];
				System.arraycopy(response, 0, temp, 0, read);
				for (int i1 = 0; i1 < temp.length; i1++) {
					Assert.assertTrue(Character.isDefined((char) temp[i1]));
					char c = (char) temp[i1];
					System.out.print(c);
				}
			}
			}
			catch(SocketTimeoutException ste){
				
			}
		}
	}
}

class WorkerThread2 implements Runnable {
	ServerSocket server;
	Client client;
	Socket socket;

	public WorkerThread2(ServerSocket server, Client client) {
		this.server = server;
		this.client = client;
	}
	public WorkerThread2(ServerSocket server) {
		this.server = server;
	}
	
	public void run() {
		try {
			while(true){
			this.socket = server.accept();
			
			if(client!=null)
				client.handleRequest(socket);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Socket getSocket(){
		Socket socket = this.socket;
		this.socket = null;
		return socket;
	}
}
