package com.olivelabs.data;

import java.net.Socket;

public class Request {
	private Long hashCode;
	public String URL;
	public byte[] buffer;
	public Socket socket;
	
	public Long getHashCode(){
		if(hashCode==null || hashCode.equals(new Long(0L))){
			hashCode = Long.valueOf(URL.hashCode());
		}
		return hashCode;
	}
	
}
