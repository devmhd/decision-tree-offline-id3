package com.devmhd;

import java.io.BufferedReader;



import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class G {
	
	
	int depth = 0;


	public static final int N_RECORDS_MAX = 1000;
	public static final int N_ATTRIBS_MAX = 20;


	public static int N_RECORDS, N_ATTRIBS;


	public static int[][] allExamples;
	public static int[] allResults;

	
	public static boolean singleComplies(AttributeValuePair condition, int exampleNo){

		
		return allExamples[exampleNo][condition.attribute] == condition.value;
		

	}
	

	public static boolean complies(ArrayList<AttributeValuePair> conditions, int exampleNo){

		boolean complies = true;
		for(AttributeValuePair condition : conditions){
			if(allExamples[exampleNo][condition.attribute] != condition.value)
				complies = false;
		}

		return complies;

	}


	public static double getEntropy(ArrayList<Integer> exampleSet){


		int nYes = 0, nTotal = 0;

		for(Integer i : exampleSet){



				nTotal++;

				if (allResults[i] == 1) nYes++;

			
		}
		
		if(nYes == 0 || nTotal == nYes) return 1.0;

		double pPlus = (double) nYes / (double) nTotal;
		double pMinus = 1.0 - pPlus;
		
		
//		System.out.println("YES: " + nYes + ", NO: " + (nTotal - nYes));
//		System.out.println("PPLUS: " + pPlus + ", PMIN: " + pMinus);
		
		double entropy = - pPlus * Math.log(pPlus) / Math.log(2.0) - pMinus * Math.log(pMinus) / Math.log(2.0);

//		System.out.println("Entropy " + entropy);
		
		return entropy;

	}
	
	public static ArrayList<Integer> filterExampleList(ArrayList<Integer> parentSet, AttributeValuePair condition){
		
		
		ArrayList<Integer> filtered = new ArrayList<Integer>();
		
		for(Integer i : parentSet){
			if(singleComplies(condition, i)) filtered.add(i);
		}
		
		return filtered;
		
	}

	public static double getEntropyCoEfficients(ArrayList<Integer> exampleSet, int attribute, int value){
		
		int nYes = 0, nTotal = 0;
		
		for(Integer i : exampleSet){

		

				nTotal++;

				if (allExamples[i][attribute] == value) nYes++;

			
		}
		
//		System.out.println("Coeff " + ((double)nYes / (double)nTotal));
		return (double)nYes / (double)nTotal;
		
		
		
	}

	public static double getInfoGain(ArrayList<Integer> exampleSet, int attribute){
		
		
		double infoGain = getEntropy(exampleSet);
		
//		System.out.println("Info Gain before: " + infoGain);
		
		for(int i=0; i<10; ++i){
			
			
			infoGain -= getEntropyCoEfficients(exampleSet, attribute, i) * getEntropy(filterExampleList(exampleSet,new AttributeValuePair(attribute, i)));
		}
		
//		System.out.println("Info Gain after: " + infoGain);
		
		return infoGain;

	}
	
	
	public static int getBestAttribute(ArrayList<Integer> exampleSet, ArrayList<Integer> attributeSet){
		
		double maxInfoGain = -100000.0;
		int theAttribute = 50;
//		System.out.print("From attributes");
	//	Debug.printAttributeList(attributeSet);
		for(Integer attribute : attributeSet){
			
			double gain = getInfoGain(exampleSet, attribute);
	//		System.out.print("Gains: " + gain);
			if(gain > maxInfoGain){
				maxInfoGain = gain;
				theAttribute = attribute;
			}
		}
		
		return theAttribute;
		
	}
	
	public static boolean isExampleSetEmpty(ArrayList<AttributeValuePair> conditions){
		
		
		for(int i = 0; i<N_RECORDS; ++i){

			if(complies(conditions, i)){

				return false;

			}
		}
		
		return true;
		
	}
	
	
	


	public static Node id3(ArrayList<Integer> trainingExampleSet, ArrayList<Integer> attributeSet, int depth){
		
//		System.out.println("ID3 called, depth: " + depth);
//		Debug.printAttributeList(attributeSet);
		
		Node root = new Node();
		
		int nYes = 0, nNo = 0;
		
		for(Integer i: trainingExampleSet){

				if(allResults[i] == 1) nYes++;
				else nNo++;

		}
		
		if(nNo == 0){ 		// All Positive
			root.setType(Node.TYPE_LEAF);
			root.setResult(1);
			
		//	System.out.println("Returned: allpos");
			
			return root;
		}
		
		if(nYes == 0){		// All Negative
			root.setType(Node.TYPE_LEAF);
			root.setResult(0);
			
		//	System.out.println("Returned allneg");
			return root;
		}
		
		if(attributeSet.isEmpty()){
			
			root.setType(Node.TYPE_LEAF);
			if(nYes > nNo) root.setResult(1);
			else root.setResult(0);
			
			
	//		System.out.println("Returned attr set empty");
			
			return root;
		}
		
		
		
		
		//System.out.println("Yes No " + nYes + " " + nNo);
		// Otherwise (recursion)
		
		int A = getBestAttribute(trainingExampleSet, attributeSet);
		//System.out.println("Best attribute: " + A);
		
		
		root.setType(Node.TYPE_BODY);
		
		root.setAttribNo(A);
		
		for(int i = 0; i<10; ++i){   // For all values of A
			
		//	ArrayList<AttributeValuePair> newConditions = new ArrayList<AttributeValuePair>(conditions);
		//	newConditions.add(new AttributeValuePair(A, i));
			
			ArrayList<Integer> newSet = filterExampleList(trainingExampleSet, new AttributeValuePair(A, i));
			
			if(newSet.isEmpty()){
				
				Node childNode = new Node();
				
				childNode.setType(Node.TYPE_LEAF);
				
				if(nYes > nNo) childNode.setResult(1);
				else childNode.setResult(0);
				
				root.children[i] = childNode;
				
				
			} else {
				
				ArrayList<Integer> newAttributeSet =  new ArrayList<Integer>(attributeSet);
				newAttributeSet.remove(new Integer(A));
				
				root.children[i] = id3(newSet, newAttributeSet, depth + 1);
				
			}
			
			
			
			
		}
		
		
		return root;
		
		
		
		
	}


	public static void loadProblems(String filename){


		allExamples = new int[N_RECORDS_MAX][N_ATTRIBS_MAX];
		allResults = new int[N_RECORDS_MAX];

		FileInputStream fstream;
		BufferedReader br;
		try {
			
			fstream = new FileInputStream(filename);
			br = new BufferedReader(new InputStreamReader(fstream));

			String line;

			N_RECORDS = 0;
			while ((line = br.readLine()) != null)   {



				String[] attribVals = line.split(",");
				N_ATTRIBS = attribVals.length-1;

				for(int i=0; i< attribVals.length-1; ++i){
					allExamples[N_RECORDS][i] = Integer.parseInt(attribVals[i])-1;
				}

				allResults[N_RECORDS] = Integer.parseInt(attribVals[attribVals.length-1]);

				N_RECORDS++;

				// System.out.println (strLine);
			}




			br.close();

			System.out.println("## Done loading " + N_RECORDS + "x" + N_ATTRIBS);




		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		for(int i = 0; i< N_RECORDS; ++i){

			for(int j=0; j<N_ATTRIBS; ++j){

				System.out.print(" " + allExamples[i][j]);

			}

			System.out.println("  " + allResults[i]);
		}


	}


	public static int decide(int[] attribValues, Node node){

		if(node.type == Node.TYPE_LEAF)
			return node.result;


		return decide(attribValues, node.children[attribValues[node.attribNo]]);


	}



}
