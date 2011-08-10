package org.olivelabs.loadbalancer;

import java.net.Socket;
import java.util.List;
import java.util.StringTokenizer;

public class RequestExtractor{
	private Socket socket;
	private String requestText;
	private SocketInputReader inputReader;
	private String urlPath;
	
	public RequestExtractor(Socket socket) {
		this.socket = socket;
		this.inputReader = new SocketInputReader(this.socket, true);
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
	
	public String getURLPath(){
		//TODO : Can be refactored
		if(requestText == null || requestText.isEmpty())
			throw new RuntimeException("Please run the getRequest before getting URL Path!");
		StringTokenizer tokens = new StringTokenizer(requestText, " ");
		while(tokens.hasMoreElements()){
			String element = tokens.nextToken();
			if(element.startsWith("/")){
				int queryIndex = element.indexOf('?');
				urlPath = element.substring(0,(queryIndex==-1)?element.length():queryIndex);
				break;
			}
				 
		}
		if(urlPath == null || urlPath.isEmpty())
			throw new RuntimeException("URL Path is null or empty!");
		return urlPath;
	}

}
