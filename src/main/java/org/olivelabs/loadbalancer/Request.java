package org.olivelabs.loadbalancer;

import java.net.Socket;

public class Request implements Comparable<Request>{
	public Socket SOCKET;
	public Double CLASS_LEVEL;
	public String URL;
	public String REQUEST_TEXT;
	
	public Request(Socket socket, Double classLevel, String url, String requestText){
		SOCKET = socket;
		CLASS_LEVEL = classLevel;
		URL = url;
		REQUEST_TEXT = requestText;
	}
	
	public int compareTo(Request o) {
		if(this.CLASS_LEVEL < o.CLASS_LEVEL) return -1;
		else if(this.CLASS_LEVEL > o.CLASS_LEVEL) return 1;
		else return 0;
	}
	
}
