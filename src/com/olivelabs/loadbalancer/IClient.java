package com.olivelabs.loadbalancer;



import com.olivelabs.loadbalancer.implementation.RspHandler;

public interface IClient{
		public void send(byte[] data, RspHandler handler) throws Exception;
}
