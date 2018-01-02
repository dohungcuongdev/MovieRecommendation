package com.hcmiu.thesis.mining.movielens.entities;

import java.util.List;

public class Item implements Comparable<Item>{
	private String name;
	private int support;
	
	public Item(String name, int support){
		this.name = name;
		this.support = support;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public int getSupport(){
		return support;
	}
	
	public void setSupport(int support){
		this.support = support;
	}
	
	@Override
	public int compareTo(Item compateItem){
		int compareSupport = ((Item) compateItem).getSupport();
		
		//descending order
		return compareSupport - this.support;
	}
	
	@Override
    public String toString() {
        return name + " : " + support;
    }

    public static String toString(List<Item> items) {
        String s = "";
        for(Item customer : items) {
            s += customer + "\n";
        }
        return s;
    }
}
