package com.olivelabs.examples;

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
import org.xlightweb.IHttpResponse;
import org.xlightweb.IHttpResponseHandler;
import org.xlightweb.IHttpResponseHeader;
import org.xlightweb.NonBlockingBodyDataSource;
import org.xlightweb.client.HttpClient;
import org.xlightweb.server.HttpServer;
import org.xsocket.DataConverter;
import org.xsocket.Execution;
import org.xsocket.ILifeCycle;

public class ResponseHandler implements IHttpResponseHandler{
	 private IHttpExchange exchange = null;

	   ResponseHandler(IHttpExchange exchange) {
	      this.exchange = exchange;
	   }

	   public void onResponse(IHttpResponse response) throws IOException {

	      // is body less response?
	      if (!response.hasBody()) {
	         try {
	            System.out.println(response.getResponseHeader());
	         } catch (Exception ignore) { }
	         exchange.send(response);

	      // ... no, it has a body
	      } else {
	         // get the response header and body 
	         final IHttpResponseHeader header = response.getResponseHeader(); 
	         NonBlockingBodyDataSource bodyDataSource = response.getNonBlockingBody();

	         // create a log buffer 
	         final List<ByteBuffer> logBuffer = new ArrayList<ByteBuffer>(); 

	         // send the response (header)        
	         final BodyDataSink bodyDataSink = exchange.send(response.getResponseHeader());

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

	   public void onException(IOException ioe) {
	      exchange.sendError(500);
	   }
}
