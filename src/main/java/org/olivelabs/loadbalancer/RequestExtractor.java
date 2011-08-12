package org.olivelabs.loadbalancer;

import java.net.Socket;
import java.util.List;
import java.util.StringTokenizer;

public class RequestExtractor{
	private Socket socket;
	private String requestText;
	private SocketInputReader inputReader;
	private String urlPath;
	private boolean executed = false;
	
	public RequestExtractor(Socket socket) {
		this.socket = socket;
		this.inputReader = new SocketInputReader(this.socket, true);
	}

	public String getRequest() {
		 executed = inputReader.execute();
		requestText = inputReader.getRequestText();
		return requestText;
	}
	
	public String getURLPath(){
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
