package org.olivelabs.loadbalancer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class WebClientUtil {
	static ServerSocket serverSocket;
	public static Socket getConnection(String hostname,int port){
		try {
			Socket socket = new Socket(hostname, port);
			return socket;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static Socket getServerSocket(){
		try {
		    if(serverSocket == null) serverSocket = new ServerSocket(9999);
		    Socket clientSocket = getConnection("localhost", 9999);
			Socket socket = serverSocket.accept();
			clientSocket.getOutputStream().write("GET /path1 HTTP/1.1\nContent-Type: text/html\n\n".getBytes());
			clientSocket.getOutputStream().flush();
			return socket;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static void closeServerSocket(){
		try {
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		Socket socket = getServerSocket();
		SocketInputReader inputReader = new SocketInputReader(socket, true);
		inputReader.execute();
		for(byte[] data : inputReader.getData())
			System.out.println(new String(data));
		closeServerSocket();
	}
}
