package com.olivelabs.loadbalancer;


import org.simpleframework.http.Response;

import com.ning.http.client.Request;

public interface IClient{
		public boolean sendRequest(Request request,Response response) throws Exception;

		public boolean isFinished();

		public boolean sendRequest(String request, Response response) throws Exception;
}
