package com.devmhd;

import java.util.ArrayList;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		G.loadProblems("data.csv");
		
		ArrayList<Integer> ints = new ArrayList<Integer>();
		
		for(int i = 0; i<10; ++i) ints.add(new Integer(i));
			
		
		Node root = G.id3(new ArrayList<AttributeValuePair>(), ints, 0);
		
		System.out.println("Done");
		
		

	}

}
