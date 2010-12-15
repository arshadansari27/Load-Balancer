package com.olivelabs.data;



import java.io.BufferedInputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class NodeTest {
	Node _node;
	String _host;
	String _port;
	Metric metric;
	ServerSocket serverSocket ;
	
	@Before
	public void setUp() throws Exception{
		
		_host = "www.google.com";
		_port = "80";
		metric = new Metric();
		_node = new Node(_host,_port, metric);
		serverSocket = new ServerSocket(9898);
		Executor executor = Executors.newCachedThreadPool(new ThreadFactory() {
			
			@Override
			public Thread newThread(Runnable r) {
				Thread t = new Thread(r);
				return t;
			}
		});
		executor.execute(_node);
	}
	
	@Test
	public void testNodeRequestSending() throws Exception{
		int length = 1024;
		
		for(int i=0;i<10;i++){
			Socket socket = new Socket("localhost",9898);
			Socket incoming = serverSocket.accept();
			_node.handleRequest(incoming);
			PrintWriter out = new PrintWriter(socket.getOutputStream());
			BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
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
}
