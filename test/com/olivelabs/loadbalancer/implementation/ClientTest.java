package com.olivelabs.loadbalancer.implementation;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;

import java.io.BufferedInputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.ning.http.client.Request;
import com.ning.http.client.RequestBuilder;
import com.ning.http.client.RequestType;
import com.olivelabs.data.IMetric;
import com.olivelabs.data.INode;
import com.olivelabs.data.Metric;
import com.olivelabs.data.metrics.MetricCalculatorByNumberOfRequest;
import com.olivelabs.loadbalancer.IBalancer;

public class ClientTest {

	public static Client client;
	public ServerSocket server;
	public Socket socket;

	@Before
	public void setUp() throws Exception {
		client = new Client(InetAddress.getByName("finicity.com"), 80);
		IMetric metric = new Metric(new MetricCalculatorByNumberOfRequest());
		client.setMetrics(metric);
		server = new ServerSocket(9999);

	}

	@Test
	public void testHandleRequest() throws Exception {
		int length = 8192;
		for (int i = 0; i < 3; i++) {
			Socket testSocket = new Socket("localhost", 9999);
			socket = server.accept();
			PrintWriter out = new PrintWriter(testSocket.getOutputStream());
			BufferedInputStream in = new BufferedInputStream(
					testSocket.getInputStream());
			out.println("GET / HTTP/1.0\n\n");
			out.flush();
			client.handleRequest(socket);
			byte[] response = new byte[length];
			int read = 0;
			ArrayList bytes = new ArrayList();
			while ((read = in.read(response)) != -1) {
				bytes.add(response);
				for (int index = 0; index < response.length; index++) {
					System.out.print((char) response[index]);
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
			//Assert.assertTrue(finalBytes.length >= count*(length-1));
			for (int index = 0; index < finalBytes.length; index++) {
				System.out.print((char) finalBytes[index]);
			}
			for (int index = 0; index < 10; index++) {
				System.out.print("*");
			}
			System.out.println("*");
			System.out.println(finalBytes.length);
		}
	}

}
