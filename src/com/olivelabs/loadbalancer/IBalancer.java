package com.olivelabs.loadbalancer;

public interface IBalancer {

	void balance(Object request, Object responseHandler);
	
}
