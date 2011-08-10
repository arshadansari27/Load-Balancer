package org.olivelabs.loadbalancer;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class RequestExtractorTest {

	Socket socket;
	WebServerUtil webserverUtil;

	@Before
	public void setUp() throws Exception {
		webserverUtil = new WebServerUtil();
		webserverUtil.startServer();
		Thread.currentThread().sleep(3000);
		socket = new Socket("localhost",9999);
		List<byte[]> data = new ArrayList<byte[]>();
		data.add("GET / HTTP1.0\n".getBytes());
		data.add("content-type: text/html\n\n".getBytes());
		SocketOutputWriter outputWriter = new SocketOutputWriter(socket, data);
		outputWriter.execute();
	}

	
	@Test
	public void testRequestExtractor(){
		
		String request = new RequestExtractor(socket).getRequest();
		Assert.assertNotNull(request);
		Assert.assertTrue(request.length() > 0);
		Assert.assertTrue(request.startsWith("HTTP/1.1 200 OK"));
		System.out.println(request);
	}
	
	@After
	public void tearDown() throws Exception {
		webserverUtil.stopServer();
	}
	
}
