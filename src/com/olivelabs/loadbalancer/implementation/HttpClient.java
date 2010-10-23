package com.olivelabs.loadbalancer.implementation;


import java.util.Map.Entry;

import org.apache.mina.core.session.IoSession;


import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.Cookie;
import com.ning.http.client.Part;
import com.ning.http.client.ProxyServer;
import com.ning.http.client.RequestBuilder;
import com.ning.http.client.RequestType;
import com.ning.http.client.Response;

import com.ning.http.client.Request;

import com.olivelabs.data.INode;
import com.olivelabs.loadbalancer.IBalancer;
import com.olivelabs.loadbalancer.IClient;
import com.olivelabs.loadbalancer.IClientHandler;

public class HttpClient implements IClient {

	private INode node;
	private IBalancer balancer;
	private IClientHandler clientHandler;
	AsyncHttpClient asyncHttpClient;
	private static final long CONNECT_TIMEOUT = 30*1000L;
	private boolean finished;
	public HttpClient(IBalancer balancer){
		this.balancer = balancer;
		clientHandler = new HttpClientHandler();
	}

	public boolean sendRequest(String request, org.simpleframework.http.Response serverResponse) throws Exception{
		return sendRequest(getRequestFromString(request), serverResponse);
	}
	
	
	private Request getRequestFromString(String request) {
		
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean sendRequest(Request request, org.simpleframework.http.Response serverResponse)
			throws Exception {
		clientHandler.setServerResponse(serverResponse);
		INode node = balancer.getNode();
		AsyncHttpClientConfig cf = new AsyncHttpClientConfig.Builder().setProxyServer(new ProxyServer(node.getHost(), node.getPort().intValue())).build();
		asyncHttpClient = new AsyncHttpClient(cf);
		finished = false;
		
	    System.out.println("Sending Request to : "+request.toString());
		asyncHttpClient.executeRequest( updateRequest(request,node), new AsyncCompletionHandler<Response>(){
	        
	        @Override
	        public Response onCompleted(Response  response) throws Exception{
	            System.out.println("Got the response");
	            System.out.println(response.getResponseBody());
	            finished = true;
	            return response;
	        }
	        
	        @Override
	        public void onThrowable(Throwable t){
	        	finished = true;
	        	System.out.println("Shit happened!");
	        }
	    });  
		return true;
	}

	public String getNewURL(String oldURL, String host, Long port){
		String subURL = oldURL.substring(oldURL.indexOf("/", 8));
		String newUrl = "http://"+host+":"+port+subURL;
		return newUrl;
	}
	public Request updateRequest(Request request, INode node){
		RequestBuilder builder = new RequestBuilder(request.getType());
		String url = request.getUrl();
		builder.setUrl(getNewURL(url, node.getHost(), node.getPort()));
		builder.setHeaders(request.getHeaders());
		if(request.getType()==RequestType.POST || request.getType()==RequestType.PUT){
			builder.setParameters(request.getParams());
			builder.setBody(request.getByteData());
		}
			
		if(request.getQueryParams()!=null){
			
			for(Entry<String, String> entry : request.getQueryParams().entries()){
				builder.addQueryParameter(entry.getKey(), entry.getValue());
			}
		}
			
		for(Cookie cookie : request.getCookies())
			builder.addCookie(cookie);
	    
	    request = builder.build();
	    return request;
		
	}
	public boolean isFinished() {
		// TODO Auto-generated method stub
		return this.finished;
	}	
}
