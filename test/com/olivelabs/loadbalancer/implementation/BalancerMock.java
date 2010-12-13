package com.olivelabs.loadbalancer.implementation;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import com.olivelabs.data.INode;
import com.olivelabs.loadbalancer.IBalancer;

public class BalancerMock implements IBalancer{

	@Override
	public INode getNode() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public INode addNode(String host, String port) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean removeNode(INode node) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeNodeById(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isNodeUp(INode n) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isNodeQueueEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getAlgorithmName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAlgorithmName(String algorithmName) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getMetricType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setMetricType(String metricType) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handle(Socket socket)  {
		WorkerThread worker = new WorkerThread(socket);
		Thread thread = new Thread(worker);
		thread.start();
	}
	
}
class WorkerThread implements Runnable{
	Socket socket;
	public WorkerThread(Socket socket){
		this.socket = socket;
	}
	public void run(){
		try {
			write(socket, read(socket));
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public byte[] read(Socket socket){
		int length = 8192;
		try {
			BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
			byte[] data = new byte[length];
			int read = in.read(data);
			do {
				System.out.println("READ DATA: "+read);
				System.out.println(data.toString());
			}while((read = in.read(data)) != -1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] finalBytes = "HTTP/1.0 200 OK\n\n<html>This is a Test</html>".getBytes();
		System.out.println("Completed Reading....");
		return finalBytes;
	}
	
	public void write(Socket socket, byte[] finalBytes){
		System.out.println("Writing Data....");
		try {
			PrintWriter out = new PrintWriter(socket.getOutputStream());
			for(int index=0; index<finalBytes.length;index++){
				out.print((char) finalBytes[index]);
			}
			out.flush();
			System.out.println("Completed Writing....");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
