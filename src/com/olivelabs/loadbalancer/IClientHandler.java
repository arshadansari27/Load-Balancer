package com.olivelabs.loadbalancer;



import org.simpleframework.http.Response;

import com.ning.http.client.AsyncHandler;

public interface IClientHandler {
	public void setServerResponse(Response response);
	public AsyncHandler getAsyncHandler();
}
