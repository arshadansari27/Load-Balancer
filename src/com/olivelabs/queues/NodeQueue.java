package com.olivelabs.queues;

import java.util.ArrayList;
import java.util.List;

import com.olivelabs.data.Node;
import com.olivelabs.routing.RoutingAlgorithm;

public class NodeQueue {

	ArrayList<Node> nodes;
	
	public NodeQueue(){
		this.nodes = new ArrayList<Node>();
	}
	public void addNode(Node node) {
		this.nodes.add(node);
	}
	public boolean removeNode(Node node){
		return this.nodes.remove(node);
	}
	public Node getNode(int index){
		return nodes.get(index);
	}
	public void addNodes(ArrayList<Node> nodes){
		this.nodes.addAll(nodes);
	}
	public boolean removeNodes(ArrayList<Node> nodes){
		return this.nodes.removeAll(nodes);
	}
	
	public Node getNodeById(Integer id){
		for(Node node : nodes){
			if(node.getId()==id) return node;
		}
		return null;
	}
	
	public List<Node> getAll(){
		return nodes;
	}
	public boolean hasNode(Node node){
		return nodes.contains(node);
	}
	public boolean isEmpty() {
		return nodes.isEmpty();
	}
	public int getSize() {
		return nodes.size();
	}
	public void addNode(Node node, int index) {
		nodes.add(index, node);
		
	}
}
