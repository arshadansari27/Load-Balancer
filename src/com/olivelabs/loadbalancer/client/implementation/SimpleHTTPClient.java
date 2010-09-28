package com.olivelabs.loadbalancer.client.implementation;

import java.io.IOException;
import java.net.ConnectException;

import org.xlightweb.IHttpRequest;
import org.xlightweb.client.HttpClient;

import com.olivelabs.loadbalancer.HttpResponseHandler;
import com.olivelabs.loadbalancer.client.Client;
import com.olivelabs.loadbalancer.server.Request;
import com.olivelabs.loadbalancer.server.ResponseHandler;
import com.olivelabs.loadbalancer.server.implementation.SimpleResponseHandler;

public class SimpleHTTPClient implements Client {

	private HttpClient httpClient;

	public HttpClient getHttpClient() throws Exception {
		if (httpClient == null)
			throw new Exception("Http Client not instantiated!");
		return httpClient;
	}

	public void setHttpClient(HttpClient client) {
		this.httpClient = client;
	}

	public void setAutoHandleCookies(boolean isAutohandlingCookies) {
		httpClient.setAutoHandleCookies(isAutohandlingCookies);

	}

	public void setFollowsRedirect(boolean isFollowsRedirect) {
		httpClient.setFollowsRedirect(isFollowsRedirect);

	}

	public void setAutoUncompress(boolean isAutoUncompress) {
		httpClient.setAutoUncompress(isAutoUncompress);

	}

	public void close() throws IOException {
		httpClient.close();
	}

	public void send(Request wrappedRequest,
			SimpleResponseHandler responseHandler) throws Exception {
		
		httpClient.send((IHttpRequest) wrappedRequest.getRequest(),
				responseHandler.getResponseHandler());

	}

}
