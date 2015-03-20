package com.devmhd;


public class Node {

	public static final int TYPE_LEAF = 0;
	public static final int TYPE_BODY = 1;

	public int result;
	public int attribNo;
	public int type;
	public Node[] children;
	
	public Node(){
		children = new Node[10];
	}

	public void setResult(int result) {
		this.result = result;
	}

	public void setAttribNo(int attribNo) {
		this.attribNo = attribNo;
	}

	public void setType(int type) {
		this.type = type;
	}


}
