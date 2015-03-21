package com.devmhd;

import java.util.ArrayList;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		G.loadProblems("data.csv");
		
		ArrayList<Integer> allAttribs = new ArrayList<Integer>();
		
		for(int i = 0; i<10; ++i) allAttribs.add(new Integer(i));
		
		
		
		
		
		for(int testCount=0; testCount<100; ++testCount){
			
			ArrayList<Integer> fullTrainingSet = new ArrayList<Integer>();
			for(int i =0; i<G.N_RECORDS; ++i) fullTrainingSet.add(new Integer(i));
			
			ArrayList<Integer> testSet = new ArrayList<Integer>();
			
			for(int i = 0; i< 133; ++i){
				
				int removeIndex = (int) (Math.random() * (683-i));
				fullTrainingSet.remove(removeIndex);
				
				testSet.add(removeIndex);
				
			}
			
			Node root = G.id3(fullTrainingSet, allAttribs, 0);
			
			
			int correct = 0;
			for(Integer testcase : testSet){
				
				if(G.decide(G.allExamples[testcase], root) == G.allResults[testcase]) correct++;
			}
			
			System.out.println("Accuracy: " + ((double)correct / 133.0 * 100));
			
		}
		
		
		
		
//		int works = 0, dontwork = 0;
//		
//		for(int i=0; i<G.N_RECORDS; ++i){
//			
//			fullTrainingSet.remove(new Integer(i));
//			Node root = G.id3(fullTrainingSet, ints, 0);
//			
//			if(G.decide(G.allExamples[i], root) == G.allResults[i])
//				works++;
//			else 
//				dontwork++;
//			
//			System.out.println("" + works + " " + dontwork);
//			
//		}
//		
//		
		
		System.out.println("Done");
		
		

	}

}
