package com.olivelabs.queues;

import java.util.ArrayList;
import java.util.List;

import com.olivelabs.data.INode;
import com.olivelabs.data.Node;
import com.olivelabs.routing.RoutingAlgorithm;

public class NodeQueue {

	ArrayList<INode> nodes;
	
	public NodeQueue(){
		this.nodes = new ArrayList<INode>();
	}
	public void addNode(INode node) {
		this.nodes.add(node);
	}
	public boolean removeNode(INode node){
		return this.nodes.remove(node);
	}
	public INode getNode(int index){
		return nodes.get(index);
	}
	public void addNodes(ArrayList<INode> nodes){
		this.nodes.addAll(nodes);
	}
	public boolean removeNodes(ArrayList<INode> nodes){
		return this.nodes.removeAll(nodes);
	}
	
	public INode getNodeById(Integer id){
		for(INode node : nodes){
			if(node.getId().equals(id)) return node;
		}
		return null;
	}
	
	public List<INode> getAll(){
		return nodes;
	}
	public boolean hasNode(INode node){
		return nodes.contains(node);
	}
	public boolean isEmpty() {
		return nodes.isEmpty();
	}
	public int getSize() {
		return nodes.size();
	}
	public void addNode(INode node, int index) {
		nodes.add(index, node);
		
	}
}
