package com.olivelabs.loadbalancer.implementation;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import com.olivelabs.loadbalancer.IClient;
import com.olivelabs.util.ByteArrayConvertor;

public class HttpClient implements IClient {

	private final InetAddress server;
	private final int port;
	private final int byteLength = 8192;
	private Socket socket;
	public HttpClient(InetAddress server, int port){
		this.server = server;
		this.port = port;
		
	}
	
	@Override
	public void send(byte[] data,
			com.olivelabs.loadbalancer.implementation.RspHandler handler)
			throws Exception {
		try {
			if(socket == null || socket.isClosed()){
				socket = new Socket(server, port); 
				socket.setKeepAlive(true);
			}
			OutputStream out = socket.getOutputStream();
			InputStream in = socket.getInputStream(); 
			out.write(data);
			out.flush();
			byte[] bufferOut = new byte[byteLength];
			ArrayList buffers = new ArrayList();
			while(in.available()!=-1){
				int status = in.read(bufferOut);
				byte[] bufferCopy = new byte[byteLength];
				System.arraycopy(bufferOut, 0, bufferCopy, 0, byteLength);
				buffers.add(bufferCopy);
				if(status == -1){
					break;
				}
				
			}
			int completeLenght = buffers.size()*byteLength;
			byte[] finalBytes = new byte[completeLenght];
			int currStart=0;
			int count=1;
			for(Object o : buffers){
				byte[] buff = (byte[]) o;
				System.arraycopy(buff, 0, finalBytes, currStart, byteLength);
				currStart = byteLength * count++;
			}
			handler.handleResponse(finalBytes);
			//System.out.println("Node: Received Response:"+ByteArrayConvertor.convertToString(finalBytes));
		} catch (Exception ioe) { 
    		throw new RuntimeException("Client failure: "+ioe.getMessage());
    	} finally {
    		try {
    			socket.close();
    		} catch (Exception e) {
    			// do nothing - server failed
    		}
    	}
		
	}
	
}
