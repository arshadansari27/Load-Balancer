package com.olivelabs.loadbalancer;




public interface IServer{
	public void startServer() throws Exception;
	public void stopServer() throws Exception;
	public void reloadServer() throws Exception;
}
