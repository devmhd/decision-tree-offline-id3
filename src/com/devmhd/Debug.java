package com.devmhd;

import java.util.ArrayList;

public class Debug {

	public static void printAttributeList(ArrayList<Integer> ints){
		
		System.out.print("AttributeSet: ");
		
		for(Integer intt : ints){
			System.out.print(" " + intt);
		}
		
		System.out.println();
		
	}

}
