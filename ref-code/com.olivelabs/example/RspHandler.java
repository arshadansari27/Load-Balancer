package com.olivelabs.example;

public class RspHandler {
	private byte[] rsp = null;
	
	public synchronized boolean handleResponse(byte[] rsp) {
		this.rsp = rsp;
		this.notify();
		return true;
	}
	
	public synchronized byte[] waitForResponse() {
		while(this.rsp == null) {
			try {
				this.wait();
			} catch (InterruptedException e) {
			}
		}
		System.out.println(this.rsp.toString());
		return this.rsp;
		//System.out.println(new String(this.rsp));
	}
	
}
