package com.devmhd;

import java.util.ArrayList;

public class Main {

	
	public static final int N_TESTS = 100;
	public static final int N_TESTSET = 133;
	
	public static void main(String[] args) {
		
		G.loadProblems("data.csv");
		
		ArrayList<Integer> allAttribs = new ArrayList<Integer>();
		
		for(int i = 0; i<10; ++i) allAttribs.add(new Integer(i));
		
		
		
		double precisionSum = 0.0, recallSum = 0.0, accuracySum = 0.0, fMeasureSum = 0.0, gMeanSum = 0.0; 
	
		
		for(int testCount=0; testCount<N_TESTS; ++testCount){
			
			ArrayList<Integer> fullTrainingSet = new ArrayList<Integer>();
			for(int i =0; i<G.N_RECORDS; ++i) fullTrainingSet.add(new Integer(i));
			
			ArrayList<Integer> testSet = new ArrayList<Integer>();
			
			for(int i = 0; i< N_TESTSET; ++i){
				
				int removeIndex = (int) (Math.random() * (G.N_RECORDS-i));
				fullTrainingSet.remove(removeIndex);
				
				testSet.add(removeIndex);
				
			}
			
			Node root = G.id3(fullTrainingSet, allAttribs, 0);
			
			
			int nFalsePositive = 0, nTruePositive = 0, nFalseNegative = 0, nTrueNegative=0;
			for(Integer testcase : testSet){
				
				int decision = G.decide(G.allExamples[testcase], root);
				int real = G.allResults[testcase];
				
				if(decision == 1){
					if(real == 1) nTruePositive++;
					else nFalsePositive++;
				} else {
					if(real == 1) nFalseNegative++;
					else nTrueNegative++;
					
				}
				
				
				
			}
			
			
			double precision = (double)nTruePositive / (double)(nTruePositive+nFalsePositive);
			double recall = (double)nTruePositive / (double)(nTruePositive+nFalseNegative);
			double accuracy = (double)(nTruePositive + nTrueNegative) / (double) testSet.size();
			
			double fMeasure = 2.0 * precision * recall / (precision + recall);
			double gMean = Math.sqrt(precision * recall);
			
			precisionSum += precision;
			recallSum += recall;
			accuracySum += accuracy;
			fMeasureSum += fMeasure;
			gMeanSum += gMean;
			
			
			System.out.println("Precision : " + precision + " Recall : " + recall + " Accuracy : " + accuracy + " FMeasure : " + fMeasure + " GMean : " + gMean);
			
			
		}
		
		
		System.out.println("\nAverage Precision : " + (precisionSum / (double)N_TESTS) + "\nAverage Recall : " + (recallSum / (double)N_TESTS) + "\nAverage Accuracy : " + (accuracySum / (double)N_TESTS) + "\nAverage F Measure : " + (fMeasureSum / (double)N_TESTS) + "\nAverage GMean : " + (gMeanSum / (double)N_TESTS));
		
		
		
		
		

	}

}
