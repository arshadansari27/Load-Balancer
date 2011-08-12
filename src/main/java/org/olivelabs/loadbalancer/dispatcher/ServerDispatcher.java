package org.olivelabs.loadbalancer.dispatcher;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

import org.olivelabs.loadbalancer.Constants;
import org.olivelabs.loadbalancer.Node;
import org.olivelabs.loadbalancer.Request;
import org.olivelabs.loadbalancer.RequestClass;
import org.olivelabs.loadbalancer.SocketInputReader;
import org.olivelabs.loadbalancer.SocketOutputWriter;
import org.omg.PortableServer.REQUEST_PROCESSING_POLICY_ID;

public class ServerDispatcher implements Dispatcher {

	private PriorityBlockingQueue<Node> nodes;
	private ConcurrentHashMap<Integer, RequestClass> requestClass;
	private Request request;
	
	public ServerDispatcher(ConcurrentHashMap<Integer, RequestClass> requestClass, PriorityBlockingQueue<Node> nodes, Request request){
		this.request = request;
		this.requestClass = requestClass;
		this.nodes = nodes;
	}
	public void run() {
		try {
			long executionTime = System.currentTimeMillis();
			Node node = nodes.take();
			List<byte[]> requestData = new ArrayList<byte[]>();
			requestData.add(request.REQUEST_TEXT.getBytes());
			System.out.println(request.REQUEST_TEXT);
			request.REQUEST_TEXT = request.REQUEST_TEXT.replaceAll("/1.1", "/1.0");
			System.out.println(request.REQUEST_TEXT);
			Socket serverConnectionSocket = new Socket(node.hostName,node.port); 
			SocketOutputWriter serverOutputWriter = new SocketOutputWriter(serverConnectionSocket, requestData);
			SocketInputReader serverInputReader = new SocketInputReader(serverConnectionSocket, false);
			serverOutputWriter.execute();
			serverInputReader.setVersion1(request.REQUEST_TEXT.indexOf("HTTP/1.1")>0 || request.REQUEST_TEXT.indexOf("HTTP1.1")>0);
			serverInputReader.execute();
			serverConnectionSocket.close();
			List<byte[]> responseData = serverInputReader.getData();
			
			for(byte[] dataToSend : responseData){
				System.out.println("***********************");
				System.out.println("Response is : ");
				System.out.println("***********************");
				System.out.println(new String(dataToSend));
				System.out.println("***********************");
			}
			
			SocketOutputWriter clientOutputReader = new SocketOutputWriter(request.SOCKET, responseData);
			clientOutputReader.execute();
			//request.SOCKET.close();
			node.updateNodeWithNewRequest();
			nodes.offer(node);
			
			executionTime = System.currentTimeMillis() - executionTime;
			requestClass.get(request.URL.hashCode()).updateAverageServiceTime(executionTime);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
