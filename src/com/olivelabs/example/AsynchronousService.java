package com.olivelabs.example;

import org.simpleframework.http.core.Container;
import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;
import org.simpleframework.util.thread.Scheduler;
import org.simpleframework.http.Response;
import org.simpleframework.http.Request;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.io.IOException;
import java.io.PrintStream;

public class AsynchronousService implements Container {
	static Scheduler queue;
   public static class Task implements Runnable {
  
      private final Response response;
      private final Request request;
 
      public Task(Request request, Response response) {
         this.response = response;
         this.request = request;
      }

      public void run() {
        PrintStream body;
		try {
			body = response.getPrintStream();
		
         long time = System.currentTimeMillis();

         response.set("Content-Type", "text/plain");
         response.set("Server", "HelloWorld/1.0 (Simple 4.0)");
         response.setDate("Date", time);
         response.setDate("Last-Modified", time);

         body.println("Hello World");
         body.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      }
   } 

   public AsynchronousService(Scheduler queue) {
      this.queue = queue;
   }

   public void handle(Request request, Response response) {
      Task task = new Task(request, response);
      
      queue.execute(task);
   }

   public static void main(String[] list) throws Exception {
	   
      Scheduler scheduler = new Scheduler(10);
      Container container = new AsynchronousService(scheduler);
      Connection connection = new SocketConnection(container);
      SocketAddress address = new InetSocketAddress(8080);

      connection.connect(address);
   }
}