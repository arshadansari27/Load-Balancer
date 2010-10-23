package com.olivelabs.example;
import com.ning.http.client.*;

import java.io.IOException;
import java.util.concurrent.Future;

public class JustAClient {
public static void main(String args[]) throws IOException{
	AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
    asyncHttpClient.prepareGet("http://www.ning.com/ ").execute(new AsyncCompletionHandler<Response>(){
        
        @Override
        public Response onCompleted(Response  response) throws Exception{
            System.out.println("Got the response");
            System.out.println(response.getResponseBody());
            return response;
        }
        
        @Override
        public void onThrowable(Throwable t){
        	System.out.println("Shit happened!");
        }
    });
    
}
}
