package org.olivelabs.loadbalancer.dispatcher;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

import org.olivelabs.loadbalancer.Node;
import org.olivelabs.loadbalancer.Request;
import org.olivelabs.loadbalancer.RequestClass;

public class DispatcherFactor {

	public static DispatcherType DISPATCHER_TYPE = DispatcherType.PRINT_ONLY;
	
	public static Dispatcher getDispatcher(ConcurrentHashMap<Integer, RequestClass> requestClass, PriorityBlockingQueue<Node> nodes, Request request){
		if(DISPATCHER_TYPE == DispatcherType.PRINT_ONLY)
			return new LocalDispatcher(nodes, request);
		if(DISPATCHER_TYPE == DispatcherType.HANDLE_REQUEST)
			return new ServerDispatcher(requestClass,nodes, request);
		return null;
	}
}
