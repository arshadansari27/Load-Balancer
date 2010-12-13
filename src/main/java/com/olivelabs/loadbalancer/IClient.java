package com.olivelabs.loadbalancer;



import java.net.Socket;

import com.olivelabs.data.IMetric;


public interface IClient{
		public void handleRequest(Socket next) throws Exception;
		public void setMetrics(IMetric metric);
}
