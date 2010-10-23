package com.olivelabs.loadbalancer.implementation;

import java.io.IOException;
import java.io.PrintStream;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;

import com.ning.http.client.RequestBuilder;
import com.ning.http.client.RequestType;
import com.olivelabs.loadbalancer.IClient;

public class HttpWorker implements Runnable {

	private Response response;
	private Request request;
	private IClient client;
	public HttpWorker(Request request, Response response, IClient client) {
		this.response = response;
		this.request = request;
		this.client  = client;
	}

	@Override
	public void run() {
		
		if(client!=null){
			//TODO: Add Client communication Code.. 
		}
		
		PrintStream body;
		try {
			body = response.getPrintStream();
			getRequestAsString(request);
			System.out.println("HttpWorker:Got a request to process");
			long time = System.currentTimeMillis();

			response.set("Content-Type", "text/plain");
			response.set("Server", "HelloWorld/1.0 (Simple 4.0)");
			response.setDate("Date", time);
			response.setDate("Last-Modified", time);

			body.println("Hello World");
			body.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String getRequestAsString(Request request){
		String requestMethod = request.getMethod();
		RequestType requestType = RequestType.GET;
		if(("POST".equals(requestMethod))) requestType = RequestType.POST;
		if(("PUT".equals(requestMethod))) requestType = RequestType.PUT;
		if(("DELETE".equals(requestMethod))) requestType = RequestType.DELETE;
		if(("HEAD".equals(requestMethod))) requestType = RequestType.HEAD;
		if(("OPTIONS".equals(requestMethod))) requestType = RequestType.OPTIONS;
		
		RequestBuilder requestBuilder = new RequestBuilder(requestType);
		
//		requestBuilder.setUrl(request.getAddress().get)
		
		
		System.out.println(request.getMethod());
		System.out.println(request.getAddress().toString());
		System.out.println(request.getAddress().getDomain());
		System.out.println(request.getAddress().getPort());
		System.out.println(request.getAddress().getScheme());
		System.out.println(request.getAddress().getPath());
		System.out.println(request.getAddress().getQuery());
		System.out.println(request.getAddress().getParameters().getKeys().toArray());
		System.out.println(request.getTarget());
		try {
			System.out.println("CONTENT:"+request.getContent()+"\nENDOFCONTENT");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(request.getInputStream().read());
		
		return null;
	}
}
