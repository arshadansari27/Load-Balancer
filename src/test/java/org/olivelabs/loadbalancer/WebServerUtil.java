package org.olivelabs.loadbalancer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;

public class WebServerUtil {

	private ServerSocket serverSocket;
	private boolean keepRunning = false;
	Thread thread;
	String httpHeader = "HTTP/1.1 200 OK\n"+
	"Date: Tue, 09 Aug 2011 20:59:51 GMT\n"+
	"Server: Apache/1.3.27 (Unix)  (Red-Hat/Linux) mod_jk mod_ssl/2.8.12 OpenSSL/0.9.6m\n"+
	"Last-Modified: Thu, 24 Mar 2011 07:13:07 GMT\n"+
	"ETag: \"16025-383-4d8aef03\"\n"+
	"Accept-Ranges: bytes\n"+
	"Content-Length: 899\n"+
	"Connection: close\n"+
	"Content-Type: text/html\n\n";
	
	public WebServerUtil(){
		try {
			serverSocket = new ServerSocket(9999);
			thread = new Thread(new Runnable(){
				public void run(){
					Socket socket = null;
					while(keepRunning){
						try {
							try {
								Thread.currentThread().sleep(2000);
							} catch (InterruptedException e) {
								if(!keepRunning)
									break;
								e.printStackTrace();
							}
							socket = serverSocket.accept();
							InputStream inputStream = socket.getInputStream();
							OutputStream outputStream = socket.getOutputStream();
							
							byte[] readData = new byte[100];
							StringBuilder responseBuilder = new StringBuilder();
							int bytesRead = -1;
							while ((bytesRead = inputStream.read(readData)) > 0) {
								byte[] actualRead = Arrays.copyOf(readData, bytesRead);
								String incoming = new String(actualRead);
								responseBuilder.append((incoming));
								if(incoming.endsWith("\n\n")) break;
							}
							System.out.println("Received request : "+ responseBuilder.toString());
							
							outputStream.write(httpHeader.getBytes());
							outputStream.write(body.getBytes());
							outputStream.flush();
							
							
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						finally{
							if (socket!= null)
								try {
									socket.close();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
						}
					}
				}
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void startServer(){
		keepRunning = true;
		thread.start();
	}
	
	public void stopServer(){
		keepRunning = false;
		thread.stop();
	}
	
	private String body = "" +
			"<HTML>\n<HEAD>\n<TITLE>Test Page</TITLE>\n</HEAD>"+
			"<BODY leftmargin='0' rightmargin='0' topmargin='0' marginwidth='0' marginheight='0'>"+
			"<p>This is just a Test</p>" +
			"</BODY>" +
			"</HTML>";
	
	public static void main(String[] args){
		WebServerUtil webServerUtil = new WebServerUtil();
		webServerUtil.startServer();
		Socket socket = null;
		try {
			socket = new Socket("localhost",9999);
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();
			
			outputStream.write("GET / HTTP1.0\n\n".getBytes());
			outputStream.flush();
			
			
			byte[] readData = new byte[100];
			StringBuilder responseBuilder = new StringBuilder();
			int bytesRead = -1;
			while ((bytesRead = inputStream.read(readData)) > 0) {
				responseBuilder.append((new String(readData)));
			}
			System.out.println("Received response : "+ responseBuilder.toString());
			
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			try {
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		webServerUtil.stopServer();
		
	}
}
