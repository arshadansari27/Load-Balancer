package com.olivelabs.loadbalancer.logging;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.xlightweb.BadMessageException;
import org.xlightweb.BodyDataSink;
import org.xlightweb.BodyForwarder;
import org.xlightweb.IHttpExchange;
import org.xlightweb.IHttpRequest;
import org.xlightweb.IHttpRequestHandler;
import org.xlightweb.IHttpRequestHeader;
import org.xlightweb.NonBlockingBodyDataSource;
import org.xlightweb.client.HttpClient;
import org.xlightweb.server.HttpServer;
import org.xsocket.DataConverter;
import org.xsocket.Execution;
import org.xsocket.ILifeCycle;


public class LogRequestHandler  implements IHttpRequestHandler{
	public void onRequest(final IHttpExchange exchange) throws IOException, BadMessageException {

	      IHttpRequest req = exchange.getRequest(); 

	      // is body less request?
	      if (!req.hasBody()) {
	         System.out.println(req.getRequestHeader().toString());
	         exchange.forward(req, new LogResponseHandler(exchange));

	      // .. no, the request do have a body
	      } else {
	         // get the request header and body 
	         final IHttpRequestHeader header = req.getRequestHeader(); 
	         NonBlockingBodyDataSource bodyDataSource = req.getNonBlockingBody();

	         // create a log buffer 
	         final List<ByteBuffer> logBuffer = new ArrayList<ByteBuffer>(); 

	         // forward the request (header) 
	         final BodyDataSink bodyDataSink = exchange.forward(req.getRequestHeader(), new LogResponseHandler(exchange));

	         // define a forward handler 
	         BodyForwarder bodyForwardHandler = new BodyForwarder(bodyDataSource, bodyDataSink) {

	            @Override
	            public void onData(NonBlockingBodyDataSource source, BodyDataSink sink) throws IOException {
	               ByteBuffer[] bufs = source.readByteBufferByLength(source.available());

	               for (ByteBuffer byteBuffer : bufs) {
	                  logBuffer.add(byteBuffer.duplicate());
	               }

	               sink.write(bufs);
	            }

	            @Override
	            public void onComplete() {
	               System.out.println(header.toString());
	               try {
	                  System.out.println(header.toString() + 
	                                     DataConverter.toString(logBuffer, header.getCharacterEncoding()));
	               } catch (Exception e) {
	                  System.out.println("<body not printable>");
	               }
	            }
	         };

	         // an set it 
	         bodyDataSource.setDataHandler(bodyForwardHandler);
	      }  
	   }
	} 
