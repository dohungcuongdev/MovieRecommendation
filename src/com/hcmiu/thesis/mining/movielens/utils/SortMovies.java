package com.hcmiu.thesis.mining.movielens.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortMovies {

	public static void SortByTimestamp(List<ArrayList<String>> csvLines){
		Collections.sort(csvLines, new Comparator<ArrayList<String>>() {
			@Override
		    public int compare(ArrayList<String> csvLine1, ArrayList<String> csvLine2) {
		        return Integer.valueOf(csvLine1.get(3)).compareTo(Integer.valueOf(csvLine2.get(3)));

		    }
		});
	}
}
