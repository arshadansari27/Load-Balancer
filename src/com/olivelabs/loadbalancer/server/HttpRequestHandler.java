package com.olivelabs.loadbalancer.server;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;
import java.net.UnknownHostException;

import org.xlightweb.BadMessageException;
import org.xlightweb.IHttpExchange;
import org.xlightweb.IHttpRequest;
import org.xlightweb.IHttpRequestHandler;
import org.xlightweb.IHttpResponseHandler;
import org.xlightweb.RequestHandlerChain;
import org.xlightweb.client.HttpClient;
import org.xlightweb.server.HttpServer;
import org.xsocket.Execution;
import org.xsocket.ILifeCycle;

import com.olivelabs.data.INode;
import com.olivelabs.data.Node;
import com.olivelabs.loadbalancer.IBalancer;
import com.olivelabs.routing.RoutingAlgorithm;

public class HttpRequestHandler implements IHttpRequestHandler, ILifeCycle {
	private IBalancer balancer;
	private HttpClient httpClient = null;

	public HttpRequestHandler(IBalancer bal) {
		balancer = bal;
	}

	@SuppressWarnings("deprecation")
	public void onInit() {
		httpClient = new HttpClient();
		httpClient.setAutoHandleCookies(false); // cookie auto handling has to
												// be deactivated!
		httpClient.setFollowsRedirect(false);
		httpClient.setAutoUncompress(false);
	}

	public void onDestroy() throws IOException {
		httpClient.close();
	}

	@Execution(Execution.MULTITHREADED)
	public void onRequest(IHttpExchange exchange) throws IOException,
			BadMessageException {

		
		try {
			IHttpRequest request = exchange.getRequest();
			INode node;
			node = balancer.getNode();
			

			URL url = request.getRequestUrl();
			request.setRequestUrl(new URL(url.getProtocol(), node.getHost(), node
					.getPort().intValue(), url.getFile()));
			IHttpResponseHandler responseHandler = new HttpResponseHandler(exchange);
			httpClient.send(request, responseHandler);
		} catch (ConnectException ce) {
			exchange.sendError(502, ce.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			exchange.sendError(500, e.getMessage());
		}

	}
}
