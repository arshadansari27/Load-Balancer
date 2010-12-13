package com.olivelabs.loadbalancer.implementation;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;

public class ServerTest {

	static Server server;
	@BeforeClass
	public static void setUp() throws Exception {
		server = new Server(8888);
		server.setBalancer(new BalancerMock());
		server.startServer();
	}

	@Test
	public void testSendRequest() throws  InterruptedException{
		Socket socket;
		try {
			socket = new Socket("localhost",8888);
			PrintWriter writer = new PrintWriter(socket.getOutputStream());
			BufferedInputStream reader = new BufferedInputStream(socket.getInputStream());
			writer.write("GET / HTTP1.0\n\n");
			writer.flush();
			byte[] byteBuffer = new byte[1024];
			int status	 = reader.read(byteBuffer);
			do{
				for(int i=0;i<byteBuffer.length;i++)
					System.out.print((char) byteBuffer[i]);
			
			}while((status = reader.read(byteBuffer))!=-1);
			Thread.currentThread().join();
			socket.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
	
	@Ignore
	@Test
	public void testRestart() throws Exception {
		Thread.currentThread().sleep(1000);
		server.reloadServer();
	}
	@AfterClass
	public static void tearDown() throws Exception {
		Thread.currentThread().sleep(1000);
		server.stopServer();
	}

}
