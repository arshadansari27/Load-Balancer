package com.olivelabs.loadbalancer.implementation;



import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.olivelabs.loadbalancer.implementation.Server;

public class ServerTest {

	Server server;
	@Before
	public void setUp() throws Exception {
		server = new Server(9797,1);
		server.startServer();
	}

	@Test
	public void testSendRequest() throws  InterruptedException, UnknownHostException, IOException{
		synchronized(this){
			Thread.currentThread().sleep(3000);
		}
			Socket socket = new Socket("localhost",9797);
			PrintWriter writer = new PrintWriter(socket.getOutputStream());
			BufferedInputStream reader = new BufferedInputStream(socket.getInputStream());
			writer.println("GET / HTTP1.0\r\n\r\n");
			
			writer.flush();
			byte[] byteBuffer = new byte[1024];
			int status	 = reader.read(byteBuffer);
			do{
				for(int i=0;i<byteBuffer.length;i++)
					Assert.assertTrue(Character.isDefined((char) byteBuffer[i] ));
			}while((status = reader.read(byteBuffer))!=-1);
			socket.close();
		
		
	}
	
	
	@Ignore
	@Test
	public void testRestart() throws Exception{
		server.reloadServer();
	}
	
	@After
	public void tearDown() throws Exception {
		try {
			Thread.currentThread().sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		server.stopServer();
	}

}
