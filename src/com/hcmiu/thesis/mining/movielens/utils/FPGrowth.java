package com.hcmiu.thesis.mining.movielens.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.hcmiu.thesis.mining.movielens.entities.TreeNode;
import com.hcmiu.thesis.mining.movielens.home.Main;

public class FPGrowth {
	private ArrayList<ArrayList<String>> TestData;

	/**
	 * 
	 * @Title: buildHeadTable 
	 * @Description: build head table for FP tree
	 * @param @param imtemSet
	 * @param @return
	 * @return ArrayList<TreeNode>
	 * @throws
	 */
	public ArrayList<TreeNode> buildHeadTable(ArrayList<ArrayList<String>> itemSet) {
		ArrayList<TreeNode> head = new ArrayList<TreeNode>();
		TestData = new ArrayList<ArrayList<String>>();
		Map<String, Integer> itemMap = GenerateMap(itemSet);

		Iterator<String> ite = itemMap.keySet().iterator();
		String key;
		List<String> abandonSet = new ArrayList<String>();
		while(ite.hasNext()) {
			key = (String)ite.next();
			if((itemMap.get(key)/(double)Main.numTransactions) < Constants.MIN_SUPPORT) {
				ite.remove();
				abandonSet.add(key);
			} else {
				TreeNode tn = new TreeNode();
				tn.setName(key);
				tn.setCount(itemMap.get(key));
				head.add(tn);
			}
		}

		for(ArrayList<String> items : itemSet) {
			items.removeAll(abandonSet);
		}
		
		for(ArrayList<String> items : TestData) {
			items.removeAll(abandonSet);
		}

		Collections.sort(head, new Comparator<TreeNode>() {
			@Override
			public int compare(TreeNode key1, TreeNode key2) {
				return key2.getCount() - key1.getCount();
			}
		});
		return head;
	}
	
	public Map<String, Integer> GenerateMap(ArrayList<ArrayList<String>> itemSet){
		Map<String, Integer> itemMap = new HashMap<String, Integer>();
		int i = 0;
		double testBegin = Main.numTransactions * Constants.TEST_PERCENT * (Main.TestPara - 1);
		double testEnd = Main.numTransactions * Constants.TEST_PERCENT * Main.TestPara;
		for(ArrayList<String> items : itemSet) {			
			if(i < testBegin || i > testEnd ){
				for(String item : items) {
					if(itemMap.get(item) == null) {
						itemMap.put(item, 1);
					} else {
						itemMap.put(item, itemMap.get(item) + 1);
					}
				}
			} else {
				TestData.add(items);
			}
			i++;
		}
		for(ArrayList<String> items : TestData)
			itemSet.remove(items);	
		return itemMap;
	}

	/**
	 * 
	 * @Title: findChildNode 
	 * @Description: find position for an item as build a FP tree 
	 * @param @param item
	 * @param @param curNode
	 * @param @return
	 * @return TreeNode
	 * @throws
	 */
	public TreeNode findChildNode(String item, TreeNode curNode) {
		List<TreeNode> childs = curNode.getChildNodes();
		if(null != childs) {
			for(TreeNode tn : curNode.getChildNodes()) {
				if(tn.getName().equals(item)) {
					return tn;
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * @Title: addAdjNode 
	 * @Description: link the nodes with the same name to the head table 
	 * @param 
	 * @return void
	 * @throws
	 */
	public void addAdjNode(TreeNode tn, ArrayList<TreeNode> head) {
		TreeNode curNode = null;
		for(TreeNode node : head) {
			if(node.getName().equals(tn.getName())) {
				curNode = node;
				while(null != curNode.getNextNode()) {
					curNode = curNode.getNextNode();
				}
				curNode.setNextNode(tn);
			}
		}
	}

	/**
	 * 
	 * @Title: buildFPTree 
	 * @Description: build FP tree
	 * @param @param itemSet
	 * @param @param head
	 * @param @return
	 * @return TreeNode
	 * @throws
	 */
	public TreeNode buildFPTree(ArrayList<ArrayList<String>> itemSet, ArrayList<TreeNode> head) {
		TreeNode root = new TreeNode();
		TreeNode curNode = root;
		
		for(ArrayList<String> items : itemSet) {
				for(String item : items) {
					TreeNode tmp = findChildNode(item, curNode);
					if(null == tmp) {
						tmp = new TreeNode();
						tmp.setName(item);
						tmp.setParentNode(curNode);
						curNode.getChildNodes().add(tmp);
						addAdjNode(tmp, head);
					} else {
						tmp.increaseCount();
					}
					curNode = tmp;
					
				}
				curNode = root;
		}
		return root;
	}

	/**
	 * 
	 * @Title: FPAlgo 
	 * @Description: TODO
	 * @param @param itemSet
	 * @param @param candidatePattern
	 * @return void
	 * @throws
	 */
	public void FPAlgo(ArrayList<ArrayList<String>> itemSet) {
		// build head table
		ArrayList<TreeNode> head = buildHeadTable(itemSet);

		// build FP tree
		TreeNode root = buildFPTree(itemSet, head);

		// recursion exit
		if(root.getChildNodes().size() == 0) { 
			return;
		}

		Main.treetime = System.currentTimeMillis() - Main.startTime - Main.currentTime;
		Main.currentTime += Main.treetime;
		
		Evaluate(head,1);
		Main.E1Time = System.currentTimeMillis() - Main.startTime - Main.currentTime;
		Main.currentTime += Main.E1Time;
		
		Evaluate(head,5);
		Main.E5Time = System.currentTimeMillis() - Main.startTime - Main.currentTime;
		Main.currentTime += Main.E5Time;
		 
		TestData.clear();
		itemSet.clear();
	}

	public void Evaluate(ArrayList<TreeNode> head, int m){
		int positive = 0;
		int nagative = 0;
		for(ArrayList<String> data : TestData){
			ArrayList<String> Candidates = new ArrayList<String>();
			ArrayList<String> CompareCandidates = new ArrayList<String>();
			for(int j = 0; j<=m && j < data.size() ; j++){
				CompareCandidates.add(data.get(j));
			}
			for(int i = 0; i< data.size() -1; i++){
				if(i > 0 && i < data.size() - m){
					CompareCandidates.add(data.get(i + m));
				}				
				Candidates.add(data.get(i));
				ArrayList<String> predict = new ArrayList<String>();
				ArrayList<TreeNode> predictCandidates = PredictList(head, Candidates);
				
				int predictCount = 0;
				for(TreeNode predictNode : predictCandidates){
					if(predictCount == Constants.TOP_N )
						break;
					predict.add(predictNode.getName());
					predictCount++;
				}
				
				int predictSize = predict.size();
				predict.removeAll(CompareCandidates);
				if(predictSize != predict.size()){
					positive++;
				} else {
					nagative++;
				}
			}
		}
		
		double evaluate = positive/(double)(positive + nagative);

		if(m == 1){
			Main.Evaluation1 += evaluate;
		}
		if(m == 5){
			Main.Evaluation5 += evaluate;
		}	
	}
	
	public ArrayList<TreeNode> PredictList(ArrayList<TreeNode> head, ArrayList<String> Candidates){
		List<String> headNode = new ArrayList<String>();
		for(TreeNode node : head){
			headNode.add(node.getName());
		}
		
		for(String candidate : new ArrayList<>(Candidates)){
			if(!headNode.contains(candidate))
				Candidates.remove(candidate);
		}
		
		ArrayList<ArrayList<TreeNode>> predictNodesList = new ArrayList<ArrayList<TreeNode>>();
		for(String item : Candidates){
			ArrayList<TreeNode> predictNodes = new ArrayList<TreeNode>();
			for(TreeNode node : head){
				if(node.getName().equals(item)){
					TreeNode curNode = node.getNextNode();
					while(curNode != null){
						TreeNode parent = curNode.getParentNode();
						while(parent.getName() != null){
							boolean isParentExistInPredict = false;
							for(TreeNode predictNode : predictNodes ){
								if(predictNode.getName().equals(parent.getName())){
									int count = predictNode.getCount() + curNode.getCount();
									predictNode.setCount(count);
									isParentExistInPredict = true;
								}
							}
							if(!isParentExistInPredict){
								TreeNode predictNode = new TreeNode();
								predictNode.setName(parent.getName());
								predictNode.setCount(curNode.getCount());
								predictNodes.add(predictNode);
							}
							parent = parent.getParentNode();
						}

						List<TreeNode> childNodes = curNode.getChildNodes();
						boolean isParentExistInPredict = false;
						for(TreeNode child : childNodes){
							for(TreeNode predictNode : predictNodes ){
								if(predictNode.getName().equals(child.getName())){
									int count = predictNode.getCount() + child.getCount();
									predictNode.setCount(count);
									isParentExistInPredict = true;
								}
							}
							if(!isParentExistInPredict){
								TreeNode predictNode = new TreeNode();
								predictNode.setName(child.getName());
								predictNode.setCount(child.getCount());
								predictNodes.add(predictNode);
							}
						}

						curNode = curNode.getNextNode();
					}

					predictNodesList.add(predictNodes);
					break;
				}
			}
		}

		ArrayList<TreeNode> predictNodes = new ArrayList<TreeNode>();
		ArrayList<TreeNode> abadonNodes = new ArrayList<TreeNode>();
		int maxpredictNodeCount = 0;
		for(int i=0; i<predictNodesList.size(); i++){
			if(i==0){
				predictNodes = new ArrayList<TreeNode>(predictNodesList.get(0));
				for(TreeNode predictNode : predictNodes){
					if(predictNode.getCount() > maxpredictNodeCount)
						maxpredictNodeCount = predictNode.getCount();
				}
			} else {
				for(TreeNode predictNode : predictNodes){
					boolean isNodeExist = false;
					for(TreeNode node : predictNodesList.get(i)){
						if(predictNode.getName().equals(node.getName())){
							if(predictNode.getCount() > node.getCount()){
								predictNode.setCount(node.getCount());
								if(predictNode.getCount() > maxpredictNodeCount)
									maxpredictNodeCount = predictNode.getCount();
							}
							isNodeExist = true;
						}
					}
					if(!isNodeExist){
						abadonNodes.add(predictNode);
					}
				}
			}
		}

		predictNodes.removeAll(abadonNodes);

		Collections.sort(predictNodes, new Comparator<TreeNode>() {
			@Override
			public int compare(TreeNode key1, TreeNode key2) {
				return key2.getCount() - key1.getCount();
			}
		});
		
		return predictNodes;
	}
	
	/**
	 * 
	 * @Title: readFile 
	 * @Description: Read a file and split it into a array list
	 * @param @param path
	 * @param @return
	 * @param @throws IOException
	 * @return ArrayList<ArrayList<String>>
	 * @throws
	 */
	public ArrayList<ArrayList<String>> readFile(String path, String separator) throws IOException {
		Main.numTransactions = 0;
		File f = new File(path);
		BufferedReader reader = new BufferedReader(new FileReader(f));
		String str;
		ArrayList<ArrayList<String>> dataSet = new ArrayList<ArrayList<String>>();
		while((str = reader.readLine()) != null) {
			if(!"".equals(str)) {
				Main.numTransactions++;
				ArrayList<String> tmpList = new ArrayList<String>();
				String[] s = str.split(separator);
				for(int i = 0; i <s.length; i++) {
					tmpList.add(s[i]);
				}
				dataSet.add(tmpList);
			}
		}
		return dataSet;
	}
}
