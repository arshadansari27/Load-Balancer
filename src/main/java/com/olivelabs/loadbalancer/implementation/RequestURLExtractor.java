package com.olivelabs.loadbalancer.implementation;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import com.olivelabs.data.Request;

public class RequestURLExtractor {
	
	public static Request getURL(Socket socket) throws IOException{
		Request request = null;
		BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
		socket.setSoTimeout(15000);
		String url = null;
		if(socket.isConnected()){
			byte[] data = new byte[4096];
			int read = 0, index = 0;
			List<byte[]> dataList = new ArrayList<byte[]>();
			while ((read = in.read(data)) != -1) {
				index += read;
				byte[] actualData = new byte[read];
				System.arraycopy(data,0,actualData,0,read);
				dataList.add(actualData);
				if(read < 4096) break;
			}
			byte[] finalData = new byte[index];
			for(byte[] actualData : dataList)
				System.arraycopy(actualData,0,finalData,0,actualData.length);
			StringTokenizer tokenizer = new StringTokenizer(new String(finalData)," ");
			String str = ((String) tokenizer.nextElement()).toUpperCase();
			if("GET".equals(str)|| "POST".equals(str) || "HEAD".equals(str)|| "PUT".equals(str)|| "DELETE".equals(str))
				url = tokenizer.nextElement().toString();
			int quertyStringIndex = url.indexOf((int) '?');
			if(quertyStringIndex >=0)
				url = url.substring(0, quertyStringIndex);
			request = new Request();
			request.buffer = finalData;
			request.URL = url;
			request.socket = socket;
		}
		
		return request;
	}
}


