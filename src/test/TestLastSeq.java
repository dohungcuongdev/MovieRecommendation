package test;

import java.util.ArrayList;
import java.util.List;

import service.SpadeAlgorithm;

public class TestLastSeq {

	public static void main(String[] args) {
		SpadeAlgorithm s = new SpadeAlgorithm();
		ArrayList a = s.getFreSeqList(0.1, 2);
		List<Integer> l = s.getFrequentpatternsMovieID(a, "260");
		System.out.println(l);
		

	}

}
