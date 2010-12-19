package com.olivelabs.loadbalancer.implementation;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import com.olivelabs.data.IMetric;
import com.olivelabs.loadbalancer.IClient;

public class Client implements IClient {

	private final InetAddress server;
	private final int port;
	private final int byteLength = 8192;
	private Socket socket;
	private IMetric metric;
	
	public Client(InetAddress server, int port){
		this.server = server;
		this.port = port;
	}
	
	@Override
	public void handleRequest(Socket incoming) throws Exception {
		establishConnection();
		ArrayList<byte[]> data = read(incoming, true);
		metric.setRequestServedSizeInMB(calculateSizeInMB(data.size()*byteLength));
		write(socket, data);
		ArrayList<byte[]> response = read(socket, false);
		metric.setRequestServedSizeInMB(calculateSizeInMB(response.size()*byteLength));
		write(incoming,response);
		metric.setNumberOfRequestServed(Long.valueOf(1));
		if(incoming.isConnected()) incoming.close();
		if(socket.isConnected()) socket.close();
 
	}

	private void establishConnection(){
		System.out.println("Establishing Connection");
		if(socket == null || socket.isClosed()){
			try {
				socket = new Socket(server, port);
				socket.setKeepAlive(true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new RuntimeException("Unable to create connection with external server!");
			} 
			
		}
	}
	
	private Double calculateSizeInMB(int size){
		double value = (size * 1.0) / (1024*1024);
		return Double.valueOf(value);
	}
	
	@Override
	public void setMetrics(IMetric metric) {
		this.metric = metric;
		
	}
	
	private ArrayList<byte[]> read(Socket socket, boolean fromClient){
		ArrayList<byte[]> buffers = new ArrayList<byte[]>();
		try {
			BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
			//DataInputStream inStream = new DataInputStream(socket.getInputStream());
			byte[] data = new byte[byteLength];
			int read = 0;
			while((read = in.read(data)) !=-1){
				byte[] readData = new byte[read];
				System.arraycopy(data, 0, readData, 0, readData.length);
				buffers.add(readData);
				data = new byte[byteLength];
				if(read < byteLength && fromClient){
					break;
				}		
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return buffers;
	}
	
	private void write(Socket socket, ArrayList<byte[]> data){
		try {
			BufferedOutputStream outs = new BufferedOutputStream(socket.getOutputStream());
			if(socket.isConnected()){
				for(byte[] buff : data){
					outs.write(buff);
					outs.flush();
				}
				
			}
			else{
				throw new RuntimeException("Client disconnected before writing any data!");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
