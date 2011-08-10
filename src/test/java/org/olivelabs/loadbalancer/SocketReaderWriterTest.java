package org.olivelabs.loadbalancer;


import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SocketReaderWriterTest {
	
	Socket socket;
	WebServerUtil webserverUtil;

	@Before
	public void setUp() throws Exception {
		webserverUtil = new WebServerUtil();
		webserverUtil.startServer();
		Thread.currentThread().sleep(3000);
		socket = new Socket("localhost",9999);
	}

	
	@Test
	public void testReadWriteOperation(){
		List<byte[]> data = new ArrayList<byte[]>();
		data.add("GET / HTTP1.0\n".getBytes());
		data.add("content-type: text/html\n\n".getBytes());
		SocketOutputWriter outputWriter = new SocketOutputWriter(socket, data);
		SocketInputReader inputReader = new SocketInputReader(socket, false);
		outputWriter.execute();
		inputReader.execute();
		data = inputReader.getData();
		Assert.assertNotNull(data);
		Assert.assertTrue(data.size()>0);
		for(byte[] readData : data){
			System.out.println(new String(readData));
		}
	}
	
	@After
	public void tearDown() throws Exception {
		webserverUtil.stopServer();
	}

}
