package com.olivelabs.routing;

import com.olivelabs.data.INode;
import com.olivelabs.queues.NodeQueue;

public interface IRouter {
	INode getNodeByAlgorithm(NodeQueue nodeQueue);
}
