package com.olivelabs.loadbalancer.server;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.xlightweb.IHttpRequestHandler;
import org.xlightweb.RequestHandlerChain;
import org.xlightweb.server.HttpServer;

import com.olivelabs.data.INode;
import com.olivelabs.data.Metric;
import com.olivelabs.data.Node;
import com.olivelabs.loadbalancer.IBalancer;
import com.olivelabs.routing.RoutingAlgorithm;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.verify;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.Assert;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
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
	
	@BeforeClass
	public static void setUpClass() throws Exception {
		server = new Server(9999);
		server.setHandler(new HelloHandler());
		server.start();
		
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
	}

	
	
	@AfterClass
	public static void tearDownClass() throws Exception {
		server.stop();

	}

	@Test
	public void testOnRequest() throws Exception{
		String url= "http://localhost:8888/";
		String result="";
		HttpClient client = new HttpClient();
		
        //for(int j=0;j<100;j++){
        	PostMethod post = new PostMethod(url);
            //for(int i=0;i<2;i++){
            	client.executeMethod(post);
            	result = post.getResponseBodyAsString();
            	System.out.println(result);
            	Assert.assertNotNull(result);
            //}
            post.releaseConnection();
       // }
	}
}

class HelloHandler extends AbstractHandler
{

    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
        
        response.getWriter().println("<h1>Hello Greetings!!</h1>");
    }
}