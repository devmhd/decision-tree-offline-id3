package com.devmhd;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class G {


	public static final int N_RECORDS_MAX = 1000;
	public static final int N_ATTRIBS_MAX = 20;


	public static int N_RECORDS, N_ATTRIBS;


	public static int[][] allExamples;
	public static int[] allResults;


	public static boolean complies(ArrayList<AttributeValuePair> conditions, int exampleNo){

		boolean complies = true;
		for(AttributeValuePair condition : conditions){
			if(allExamples[exampleNo][condition.attribute] != condition.value)
				complies = false;
		}

		return complies;

	}


	public static double getEntropy(ArrayList<AttributeValuePair> conditions){


		int nYes = 0, nTotal = 0;

		for(int i = 0; i<N_RECORDS; ++i){

			if(complies(conditions, i)){

				nTotal++;

				if (allResults[i] == 1) nYes++;

			}
		}

		double pPlus = (double) nYes / (double) nTotal;
		double pMinus = 1.0 - pPlus;

		return pPlus * Math.log(pPlus) / Math.log(2.0) - pMinus * Math.log(pMinus) / Math.log(2.0);



	}

	public static double getEntropyCoEfficients(ArrayList<AttributeValuePair> conditions, int attribute, int value){
		
		int nYes = 0, nTotal = 0;
		
		for(int i = 0; i<N_RECORDS; ++i){

			if(complies(conditions, i)){

				nTotal++;

				if (allExamples[i][attribute] == value) nYes++;

			}
		}
		
		return (double)nYes / (double)nTotal;
		
		
		
	}

	public static double getInfoGain(ArrayList<AttributeValuePair> conditions, int attribute){
		
		
		double infoGain = getEntropy(conditions);
		
		for(int i=0; i<10; ++i){
			
			ArrayList<AttributeValuePair> newConditions = new ArrayList<AttributeValuePair>(conditions);
			newConditions.add(new AttributeValuePair(attribute, i));
			
			infoGain -= getEntropyCoEfficients(conditions, attribute, i) * getEntropy(newConditions);
		}
		
		
		return infoGain;

	}
	
	
	public static int getBestAttribute(ArrayList<AttributeValuePair> conditions, ArrayList<Integer> attributeSet){
		
		double maxInfoGain = 0.0;
		int theAttribute = 0;
		
		for(Integer attribute : attributeSet){
			
			double gain = getInfoGain(conditions, attribute);
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
	
	
	


	public static Node id3(ArrayList<AttributeValuePair> conditions, ArrayList<Integer> attributeSet){
		
		Debug.printAttributeList(attributeSet);
		
		Node root = new Node();
		
		int nYes = 0, nNo = 0;
		
		for(int i = 0; i<N_RECORDS; ++i){

			if(complies(conditions, i)){

				if(allResults[i] == 1) nYes++;
				else nNo++;

			}
		}
		
		if(nNo == 0){ 		// All Positive
			root.setType(Node.TYPE_LEAF);
			root.setResult(1);
			return root;
		}
		
		if(nYes == 0){		// All Negative
			root.setType(Node.TYPE_LEAF);
			root.setResult(0);
			return root;
			
		}
		
		if(attributeSet.isEmpty()){
			
			root.setType(Node.TYPE_LEAF);
			if(nYes > nNo) root.setResult(1);
			else root.setResult(0);
			
			return root;
		}
		
		
		
		
		
		// Otherwise (recursion)
		
		int A = getBestAttribute(conditions, attributeSet);
		
		root.setType(Node.TYPE_BODY);
		
		root.setAttribNo(A);
		
		for(int i = 0; i<10; ++i){   // For all values of A
			
			ArrayList<AttributeValuePair> newConditions = new ArrayList<AttributeValuePair>(conditions);
			newConditions.add(new AttributeValuePair(A, i));
			
			if(isExampleSetEmpty(newConditions)){
				
				Node childNode = new Node();
				
				childNode.setType(Node.TYPE_LEAF);
				
				if(nYes > nNo) childNode.setResult(1);
				else childNode.setResult(0);
				
				root.children[i] = childNode;
				
				
			} else {
				
				ArrayList<Integer> newAttributeSet =  new ArrayList<Integer>(attributeSet);
				newAttributeSet.remove(new Integer(i));
				
				root.children[i] = id3(newConditions, newAttributeSet);
				
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
