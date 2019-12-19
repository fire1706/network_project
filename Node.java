//---------------------------------Node---------------------------------------------
//
// This object has for purposes to handle creation/manipulation of File/Directory
// The main difference between File and directory is that File has some data but 
// no nextnodes where the directory has exactly the inverse
//
//Copyright (c) 2019 by Thomas BASTIN & Victor Dachet. All Rights Reserved.
//-----------------------------------------------------------------------------------------

import java.util.*;
import java.lang.*;
import java.text.*;

public class Node{
	private List<Node> nextNodes = new ArrayList<Node>();
	private Node parent = null;
	private String name = null;
	private String path = null;
	private boolean isDirectory = false;
	private boolean isFile = false;
	private boolean isRoot = false;
	private int authorized = -1;
	private byte[] data = null;
	private int dataSize = 0;
	private String date = null;
	private String dataFormalism = new String();

	/*--------------- Constructors ---------------*/
	Node(){}

	//Directory Constructor
	Node(String name, boolean isRoot, int authorized){
		this.name = name;
		this.isDirectory = true;
		this.isRoot = isRoot;
		this.path = name;
		Date d = new Date();
		SimpleDateFormat simpledateformat = new SimpleDateFormat("MM-dd-yyyy");
		this.date = simpledateformat.format(d);
		this.dataFormalism = "drwx-xr-x-\t2 user\tgroup\t \t" + this.date +"\t"+this.name;
		this.authorized = authorized;
	}

	//Directory Constructor
	Node(String name, Node parent, int authorized) throws NodeException{
		if(parent.isDirectory() == false)
			throw new NodeException("parent is not a directory");
		this.name = name;
		this.isDirectory = true;
		this.parent = parent;
		if(parent.isRoot() == true){
			this.path = parent.getPath() + name;
		}else{
			this.path = parent.getPath() + "/" + name;
		}
		Date d = new Date();
		SimpleDateFormat simpledateformat = new SimpleDateFormat("MM-dd-yyyy");
		this.date = simpledateformat.format(d);
		this.dataFormalism = "drwx-xr-x-\t2 user\tgroup\t \t" + this.date +"\t"+ this.name;	
		this.authorized = authorized;
	}
	
	//File Constructor
	Node(String name, byte[] data, Node parent, int authorized) throws NodeException{
		if(parent.isDirectory() == false)
			throw new NodeException("parent is not a directory");
		this.name = name;
		this.data = data;
		this.isFile = true;
		this.parent = parent;
		if(parent.isRoot() == true){
			this.path = parent.getPath() + name;
		}else{
			this.path = parent.getPath() + "/" + name;
		}
		this.dataSize = data.length ;
		Date d = new Date();
		SimpleDateFormat simpledateformat = new SimpleDateFormat("MM-dd-yyyy");
		this.date = simpledateformat.format(d);
		this.dataFormalism = "-rw-rw-rw-\t1 user\tgroup\t"+this.dataSize+"\t" + this.date +"\t"+ this.name;
		this.authorized = authorized;

	}

	/*--------------- Accessors ---------------*/

	public List<Node> getNextNodes(){
		return nextNodes;
	}

	public Node getParent(){
		return parent;
	}

	public String getName(){
		return name;
	}

	public String getPath(){
		return path;
	}

	public boolean isDirectory(){
		return isDirectory;
	}

	public boolean isFile(){
		return isFile;
	}

	public boolean isRoot(){
		return isRoot;
	}

	public int getAuthorized(){
		return authorized;
	}

	public synchronized byte[] getData(){
		return data;
	}
	public int getDataSize(){
		return dataSize;
	}

	public String getDate(){
		return date;
	}

	public void addNextNode(Node nextNode){
		nextNodes.add(nextNode);
		this.dataSize = this.dataSize + nextNode.getDataSize();
	}

	public String getDataFormalism(){
		return dataFormalism;
	}

	/*--------------- Methods ---------------*/

	public int getSizeContent(){
		if(isDirectory == false)
			return 0;
		return nextNodes.size();
	}

	public String[] getDirectoryContent() throws NodeException{
		if(isDirectory == false){
			throw new NodeException("Cannot get content");
		}

		if(nextNodes.isEmpty()){
			String[] str = new String[1];
			str[0] = "";
			return str;
		}else{
			int size = nextNodes.size();
			String[] str = new String[size];
			Object[] array = nextNodes.toArray();
			Node n = null;
			for(int i=0; i<size; i++){
				n = (Node) array[i];
				str[i] = n.getDataFormalism() +"\r\n";
			}
			return str;
		}
	}

	//remove a node belonging to the current directory
	public synchronized boolean remove(Node n){
		if(isDirectory == false)
			return false;
		if(nextNodes.contains(n) == true){
			return nextNodes.remove(n);
		}else{
			System.out.println("No such node in this directory");
			return nextNodes.remove(n);
		}

	}

	//Change the name of the current Node if and only if this is a name not alreaydy used
	//return true if name has changed, false otherwise
	public synchronized boolean changeName(String name){
		Node n = null;
		Object[] array = parent.nextNodes.toArray();
		for(int i = 0; i<parent.nextNodes.size(); i++){
			n = (Node) array[i];
			if(n.getName() == name){
				return false;
			}
		}
		this.name = name;
		if(isFile){
			this.dataFormalism = "-rw-rw-rw-\t1 user\tgroup\t"+this.dataSize+"\t" + this.date +"\t"+ this.name;
		}
		if(isDirectory){
			this.dataFormalism = "drwx-xr-x-\t2 user\tgroup\t \t" + this.date +"\t"+this.name;
		}
		
		return true;
	}



}