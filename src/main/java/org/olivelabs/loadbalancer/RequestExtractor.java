package org.olivelabs.loadbalancer;

import java.net.Socket;
import java.util.List;

public class RequestExtractor{
	private Socket socket;
	private String requestText;
	private SocketInputReader inputReader;
	
	public RequestExtractor(Socket socket) {
		this.socket = socket;
		this.inputReader = new SocketInputReader(this.socket, false);
	}

	public String getRequest() {
		inputReader.execute();
		List<byte[]> data = inputReader.getData();
		StringBuilder stringBuilder = new StringBuilder();
		for(byte[] readData : data){
			stringBuilder.append(new String(readData));
		}
		requestText = stringBuilder.toString();
		return requestText;
	}

}
