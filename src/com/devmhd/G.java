package com.devmhd;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.omg.CORBA.NameValuePair;

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
	

//	public static DecTreeNode id3(AttributeValuePair[] conditions, int targetAttribute, )
	
	
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
					allExamples[N_RECORDS][i] = Integer.parseInt(attribVals[i]);
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


	public static int decide(int[] attribValues, DecTreeNode node){

		if(node.type == DecTreeNode.TYPE_LEAF)
			return node.result;


		return decide(attribValues, node.children[attribValues[node.attribNo]]);


	}



}
