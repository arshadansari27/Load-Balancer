package com.olivelabs.loadbalancer.implementation;

import java.io.IOException;
import java.io.PrintStream;
import java.net.CookieStore;
import java.util.logging.Logger;


import org.slf4j.LoggerFactory;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHandler;
import com.ning.http.client.Cookie;
import com.ning.http.client.Headers;
import com.ning.http.client.Response;
import com.olivelabs.loadbalancer.IClientHandler;
import com.olivelabs.util.ResponseConvertor;

public class HttpClientHandler implements IClientHandler {

	private final static Logger LOGGER = Logger.getAnonymousLogger();
	private boolean finished;
	private org.simpleframework.http.Response serverResponse;
	private AsyncCompletionHandler<Response> handler = new AsyncCompletionHandler<Response>(){
        
        @Override
        public Response onCompleted(Response  response) throws Exception{
            System.out.println("Got the response");
            System.out.println(response.getResponseBody());
            
            if(serverResponse != null){
            	writeResponse(response);
            }
            finished = true;
            return response;
        }
        
        @Override
        public void onThrowable(Throwable response){
        	System.out.println("Shit happened!");
        	 if(serverResponse != null){
             	writeResponse(response);
             }
        	finished = true;
        }
        
        public void writeResponse(Throwable  response){
        	PrintStream body;
    		try {
    			body = serverResponse.getPrintStream();
    			System.out.println("HttpClient Writing on Server Response Object");
    			long time = System.currentTimeMillis();

    			serverResponse.set("Content-Type", "text/plain");
    			serverResponse.set("Server", "HelloWorld/1.0 (Simple 4.0)");
    			serverResponse.setDate("Date", time);
    			serverResponse.setDate("Last-Modified", time);

    			body.println(response.getMessage());
    			body.close();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        }
        public void writeResponse(Response  response){
        	PrintStream body;
    		try {
    			body = serverResponse.getPrintStream();
    			System.out.println("HttpClient Writing on Server Response Object");
    			for(String headerName : response.getHeaders().getHeaderNames()){
    				String headerValue = response.getHeaders().getHeaderValue(headerName);
    				serverResponse.set(headerName, headerValue);
    			}
    			for(Cookie cookie  : response.getCookies()){
    				org.simpleframework.http.Cookie cookieToSend = new org.simpleframework.http.Cookie(cookie.getName(), cookie.getValue());
    				serverResponse.setCookie(cookieToSend);
    			}
    			
    			serverResponse.setContentLength(response.getResponseBody().length());
    			
    			
    			body.println(response.getResponseBody());
    			body.close();
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
        }
    };  

   

	@Override
	public AsyncHandler<Response> getAsyncHandler() {
		return this.handler;
	}


	@Override
	public void setServerResponse(org.simpleframework.http.Response serverResponse) {
		this.serverResponse = serverResponse;
		
	}


	

}