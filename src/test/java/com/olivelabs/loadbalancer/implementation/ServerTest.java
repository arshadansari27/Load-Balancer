package com.olivelabs.loadbalancer.implementation;



import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.olivelabs.loadbalancer.implementation.Server;

public class ServerTest {

	static Server server;
	@BeforeClass
	public static void setUp() throws Exception {
		server = new Server(9999);
		server.setBalancer(new BalancerMock());
		server.startServer();
	}

	@Test
	public void testSendRequest() throws  InterruptedException{
		Socket socket;
		try {
			socket = new Socket("localhost",9999);
			PrintWriter writer = new PrintWriter(socket.getOutputStream());
			BufferedInputStream reader = new BufferedInputStream(socket.getInputStream());
			writer.println("GET / HTTP1.0\n\n");
			
			writer.flush();
			byte[] byteBuffer = new byte[1024];
			int status	 = reader.read(byteBuffer);
			do{
				for(int i=0;i<byteBuffer.length;i++)
					Assert.assertTrue(Character.isDefined((char) byteBuffer[i] ));
			
			}while((status = reader.read(byteBuffer))!=-1);
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
	public static void tearDownClass() throws Exception {
		Thread.currentThread().sleep(1000);
		server.stopServer();
	}

}
