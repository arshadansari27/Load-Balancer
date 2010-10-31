package com.olivelabs.loadbalancer;

import com.olivelabs.data.INode;
import com.olivelabs.loadbalancer.implementation.RspHandler;

public interface IClientHandler extends Runnable{
	public void call( byte[] data, RspHandler handler) throws Exception;
}
