package org.olivelabs.loadbalancer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.olivelabs.loadbalancer.dispatcher.ServerDispatcher;

public class ServerDispatcherTest {

	WebServerUtil webServerUtil;
	ServerSocket serverSocket;
	ConcurrentHashMap<Integer, RequestClass> requestClass;
	PriorityBlockingQueue<Node> nodes;
	Socket clientSocket;

	@Before
	public void setUp() throws Exception {
		webServerUtil = new WebServerUtil();
		webServerUtil.startServer();
		requestClass = new ConcurrentHashMap<Integer, RequestClass>();
		serverSocket = new ServerSocket(9998);
		nodes = new PriorityBlockingQueue<Node>();
		nodes.add(new Node("www.yahoo.co.in", 80));
		nodes.add(new Node("www.google.co.in", 80));

	}

	@Test
	public void testServerDispatcher() throws UnknownHostException, IOException {
		for (int i = 1; i <= 10; i++) {
			clientSocket = new Socket("localhost", 9998);
			clientSocket.getOutputStream().write(
					"GET / HTTP/1.1\nContent-Type: text/html\n\n".getBytes());
			clientSocket.getOutputStream().flush();
			Socket middleSocket = serverSocket.accept();
			RequestExtractor requestExtractor = new RequestExtractor(
					middleSocket);
			String requestText = requestExtractor.getRequest();
			String urlPath = requestExtractor.getURLPath();
			RequestClass reqClass = requestClass.get(urlPath.hashCode());
			if (reqClass == null) {
				reqClass = new RequestClass(urlPath);
				requestClass.put(urlPath.hashCode(), reqClass);
			}
			reqClass.increaseRequestCount();
			Request request = new Request(middleSocket, reqClass
					.getClassLevel(), urlPath, requestText);

			ServerDispatcher serverDispatcher = new ServerDispatcher(
					requestClass, nodes, request);

			serverDispatcher.run();
			for (Node node : nodes)
				System.out.println("NODE WEIGHT:" + node.weight);

			for (RequestClass rC : requestClass.values()) {
				System.out.println("RequestClass - ClassLevel : "
						+ rC.getClassLevel());
				System.out.println("RequestClass - RequestProbability: "
						+ rC.requestProbability);
				System.out.println("RequestClass - Avg Service Time: "
						+ rC.averageServiceTime);

			}

			SocketInputReader inputReader = new SocketInputReader(clientSocket,
					true);
			inputReader.execute();
			for (byte[] data : inputReader.getData())
				System.out.println(new String(data));
		}
	}

	@After
	public void tearDown() throws Exception {
		webServerUtil.stopServer();
		clientSocket.close();
		serverSocket.close();
	}

}
