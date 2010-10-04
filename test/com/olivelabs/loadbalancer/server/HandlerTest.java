package com.olivelabs.loadbalancer.server;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.xlightweb.HttpRequest;
import org.xlightweb.IHttpRequestHandler;
import org.xlightweb.IHttpResponse;
import org.xlightweb.IHttpResponseHandler;
import org.xlightweb.RequestHandlerChain;
import org.xlightweb.client.HttpClient;
import org.xlightweb.server.HttpServer;

import com.olivelabs.data.INode;
import com.olivelabs.data.Metric;
import com.olivelabs.data.Node;
import com.olivelabs.loadbalancer.IBalancer;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.verify;

import java.io.IOException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.Assert;


import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;


public class HandlerTest {

	IBalancer balancer;
	INode node;
	IHttpRequestHandler requestHandler;
	static Server server;
	static Handler handler;
    private static String baseUrl;
	public static final int TIMEOUT = 5000;
	@BeforeClass
	public static void setUpClass() throws Exception {
		server = new Server(9999);
		server.setHandler(new TestHandler());
		server.start();
		System.out.println("Starting server at 9999...");
		//server.join();
	}
	
	@Before
	public void setUp() throws Exception {
		balancer = createMock(IBalancer.class);
		INode n1 = createMock(INode.class);
		
		expect(n1.getHost()).andReturn("localhost");
		expect(n1.getPort()).andReturn(new Long(9999));
		replay(n1);
		expect(balancer.getNode()).andReturn(n1);
		replay(balancer);
		requestHandler = new HttpRequestHandler(balancer);
		HttpServer proxy =  new HttpServer(8888, requestHandler);
		proxy.setAutoCompressThresholdBytes(Integer.MAX_VALUE);
		proxy.setAutoUncompress(false);
		proxy.start();
		System.out.println("Starting balancer at 8888...");
	}

	
	
	@AfterClass
	public static void tearDownClass() throws Exception {
		server.stop();

	}

	
	@Test
	public void testOnRequest() throws Exception{
		baseUrl= "http://localhost:8888/";
		String result="";
		HttpClient client = new HttpClient();
		HttpRequest request = new HttpRequest("Get", baseUrl);
        IHttpResponseHandler responseHandler = new IHttpResponseHandler() {
			
			@Override
			public void onResponse(IHttpResponse arg0) throws IOException {
				System.out.println("Response from the server to balancer...");
				System.out.print(arg0.getStatus());
				
				
			}
			
			@Override
			public void onException(IOException arg0) throws IOException {
				System.out.print("Error: "+arg0.getMessage());
				
			}
		};
		//Thread.currentThread().wait(1000);
		
		System.out.println("Sending Request to balancer...");
        //IHttpResponse response = client.call(request);
        client.send(request,responseHandler);
        
        System.out.println("Sent Request to balancer...");
        
        Assert.assertNotNull(result);
        client.close();
	}
	
}
class TestHandler extends AbstractHandler
{

    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
        System.err.println("Sending Response...");
        response.getWriter().println("<h1>Hello Greetings!!</h1>");
    }
}
