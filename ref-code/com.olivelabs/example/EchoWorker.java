package com.olivelabs.example;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;

public class EchoWorker implements Runnable {
	private List queue = new LinkedList();
	NioClient client;
	public void processData(NioServer server, SocketChannel socket, byte[] data, int count) {
		byte[] dataCopy = new byte[count];
		System.arraycopy(data, 0, dataCopy, 0, count);
		synchronized(queue) {
			queue.add(new ServerDataEvent(server, socket, dataCopy));
			queue.notify();
		}
	}
	public EchoWorker(NioClient client){
		this.client = client;
	}
	public void run() {
		ServerDataEvent dataEvent;
		
		while(true) {
			// Wait for data to become available
			synchronized(queue) {
				while(queue.isEmpty()) {
					try {
						queue.wait();
					} catch (InterruptedException e) {
					}
				}
				dataEvent = (ServerDataEvent) queue.remove(0);
			}
			
			// Return to sender
			RspHandler handler = new RspHandler();
			try {
				client.send(dataEvent.data, handler);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dataEvent.server.send(dataEvent.socket, handler.waitForResponse() );
			StringBuilder stringBuilder = new StringBuilder();
			//char[] str = new char[dataEvent.data.length];
			//for(int i=0;i<dataEvent.data.length;i++){
				//stringBuilder.append(dataEvent.data[i]);
			//}
			
			System.out.println(dataEvent.data);
		}
	}
}
