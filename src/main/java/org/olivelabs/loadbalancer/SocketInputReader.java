package org.olivelabs.loadbalancer;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SocketInputReader {

	private Socket socket;
	private List<byte[]> data;
	private boolean listening = false;
	
	public SocketInputReader(Socket socket, boolean listening ) {
		this.socket = socket;
		this.listening = listening;
	}
	
	private boolean isExecuted = false;

	public void execute() {
		InputStream inStream;
		data = new ArrayList<byte[]>();
		try {
			inStream = socket.getInputStream();

			byte[] readData = new byte[Constants.BYTE_LENGTH];
			int bytesRead = -1;
			while ((bytesRead = inStream.read(readData)) > 0) {
				byte[] actualRead = Arrays.copyOf(readData, bytesRead);
				data.add(actualRead);
				if(listening && (new String(actualRead)).endsWith("\n\n")) break;
			}
			
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
