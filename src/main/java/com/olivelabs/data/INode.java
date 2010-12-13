package com.olivelabs.data;

import java.net.Socket;

public interface INode extends Runnable {

	public String getHost();
	public Long getPort();
	public IMetric getMetric();
	public void setMetric(IMetric metric);
	public Integer getId();
	public void setRequestServedSizeInMB(Double value);
	void setNumberOfRequestServed(Long value);
	public boolean start();
	public boolean stop();
	public void handleRequest(Socket socket) throws Exception;
}
