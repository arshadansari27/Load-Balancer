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
import java.net.SocketException;
import java.net.SocketTimeoutException;
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
	private final int byteLength = 4096;
	private Socket socket;
	private IMetric metric;

	public Client(InetAddress server, int port) {
		this.server = server;
		this.port = port;
	}

	@Override
	public void handleRequest(Socket incoming) throws Exception {
		establishConnection();
		readWrite(incoming, socket, true);
		readWrite(socket, incoming, false);
		if (incoming.isConnected())
			incoming.close();
		if (socket.isConnected())
			socket.close();

	}

	private void establishConnection() {
		try {
			socket = new Socket(server, port);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(
					"Unable to create connection with external server!");
		}
	}

	private Double calculateSizeInMB(int size) {
		double value = (size * 1.0) / (1024 * 1024);
		return Double.valueOf(value);
	}

	@Override
	public void setMetrics(IMetric metric) {
		this.metric = metric;

	}

	private void readWrite(Socket sourceSocket, Socket destinationSocket,
			boolean fromClient) {

		BufferedInputStream in;
		BufferedOutputStream outs;

		try {
			in = new BufferedInputStream(sourceSocket.getInputStream());
			outs = new BufferedOutputStream(destinationSocket.getOutputStream());
			if(fromClient) sourceSocket.setSoTimeout(5000);
			else sourceSocket.setSoTimeout(45000);
			int byteCount = 0;
			if (sourceSocket.isConnected() && destinationSocket.isConnected()) {
				byte[] data = new byte[byteLength];
				int read = 0;
				try {
					while ((read = in.read(data)) != -1) {
						byteCount += read;
						outs.write(data, 0, read);
						outs.flush();
					}
					metric.setRequestServedSizeInMB(calculateSizeInMB(byteCount));
					metric.setNumberOfRequestServed(Long.valueOf(1));
				} catch (SocketTimeoutException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					
				
				} catch (SocketException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					
				}
				 catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}
}
