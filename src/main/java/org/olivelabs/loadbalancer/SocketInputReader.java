package org.olivelabs.loadbalancer;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SocketInputReader {

	private Socket socket;
	private List<byte[]> data;
	private String requestData;
	private double sizeInKBs;
	private boolean listening = false;
	private boolean version1 = false;
	
	public SocketInputReader(Socket socket, boolean listening ) {
		this.socket = socket;
		this.listening = listening;
	}
	
	public void setVersion1(boolean version){
		this.version1 = version;
	}
	
	private boolean isExecuted = false;

	public boolean execute() {
		try {
			socket.setSoTimeout(15000);
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		InputStream inStream;
		data = new ArrayList<byte[]>();
		long sizeInBs = 0L;
		try {
			inStream = socket.getInputStream();

			byte[] readData = new byte[Constants.BYTE_LENGTH];
			int bytesRead = -1;
			StringBuilder strBuilder = new StringBuilder();
			while ((bytesRead = inStream.read(readData)) > 0) {
				sizeInBs += bytesRead;
				byte[] actualRead = Arrays.copyOf(readData, bytesRead);
				data.add(actualRead);
				String actualReadStr = new String(actualRead);
				strBuilder.append(actualReadStr);
				//System.out.println("READING :"+actualReadStr);
				if(listening && (actualReadStr).endsWith("\r\n\r\n")) break;
				if( (actualReadStr).endsWith("0\r\n\r\n")) break;
			}
			requestData = strBuilder.toString();
			System.out.println("Read from input stream:\n "+ requestData);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sizeInKBs = Math.ceil((sizeInBs * 1.0) / 1024);
		isExecuted = true;
		return isExecuted;
	}

	public List<byte[]>  getData() {
		if (isExecuted)
			return data;
		throw new RuntimeException("Please executed the command!!");

	}
	
	public String  getRequestText() {
		if (isExecuted)
			return requestData;
		throw new RuntimeException("Please executed the command!!");

	}
	
	public double getSize(){
		return sizeInKBs;
	}
}
