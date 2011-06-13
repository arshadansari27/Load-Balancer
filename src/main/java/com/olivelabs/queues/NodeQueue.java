package com.olivelabs.queues;

import java.util.ArrayList;
import java.util.List;

import com.olivelabs.data.INode;
import com.olivelabs.data.Node;

public class NodeQueue {

	ArrayList<INode> nodes;
	
	public NodeQueue(){
		this.nodes = new ArrayList<INode>();
	}
	public synchronized void addNode(INode node) {
		this.nodes.add(node);
	}
	public synchronized boolean removeNode(INode node){
		return this.nodes.remove(node);
	}
	public synchronized INode getNode(int index){
		return nodes.get(index);
	}
	public synchronized void addNodes(ArrayList<INode> nodes){
		this.nodes.addAll(nodes);
	}
	public synchronized boolean removeNodes(ArrayList<INode> nodes){
		return this.nodes.removeAll(nodes);
	}
	
	public synchronized INode getNodeById(Integer id){
		for(INode node : nodes){
			if(node.getId().equals(id)) return node;
		}
		return null;
	}
	
	public synchronized List<INode> getAll(){
		return nodes;
	}
	public synchronized boolean hasNode(INode node){
		return nodes.contains(node);
	}
	public synchronized boolean isEmpty() {
		return nodes.isEmpty();
	}
	public synchronized int getSize() {
		return nodes.size();
	}
	public synchronized void addNode(INode node, int index) {
		nodes.add(index, node);
		
	}
}
