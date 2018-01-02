package com.hcmiu.thesis.mining.movielens.entities;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {
	private String name;
	private int count;
	private TreeNode parentNode;
	private List<TreeNode> childNodes = new ArrayList<TreeNode>();
	private TreeNode nextNode;
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public int getCount(){
		return count;
	}
	
	public void setCount(int count){
		this.count = count;
	}
	
	public void increaseCount(){
		this.count = count + 1;
	}
	
	public TreeNode getParentNode(){
		return parentNode;
	}
	
	public void setParentNode(TreeNode parentNode){
		this.parentNode = parentNode;
	}
	
	public List<TreeNode> getChildNodes(){
		return childNodes;
	}
	
	public void setChildNodes(List<TreeNode> childNodes){
		this.childNodes = childNodes;
	}
	
	public TreeNode getNextNode(){
		return nextNode;
	}
	
	public void setNextNode(TreeNode nextNode){
		this.nextNode = nextNode;
	}
}
