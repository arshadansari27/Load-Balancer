package org.olivelabs.loadbalancer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

public class SocketOutputWriter {

	private Socket socket;
	private List<byte[]> data;
	
	
	public SocketOutputWriter(Socket socket, List<byte[]> data) {
		this.socket = socket;
		this.data = data;
	}
	
	private boolean isExecuted = false;

	public void execute() {
		OutputStream outputStream;
		try {
			outputStream = socket.getOutputStream();
			for(byte[] dataToWrite : data) {
				outputStream.write(dataToWrite);
			}
			outputStream.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		isExecuted = true;
	}

	public List<byte[]>  getData() {
		if (isExecuted)
			return data;
		return null;

	}
}
