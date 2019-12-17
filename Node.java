//Authors: Bastin Thomas & Dachet Victor

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

	// Node(String name){
	// 	this.name = name;
	// 	this.isDirectory = true;

	// }

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
		//this.dataFormalism = "-rwxr-xr-x 1 100 100 14757 a.out\r\n"	;

	}


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
		this.dataSize = data.length;
		//this.dataFormalism = "-rwxr-xr-x 1 100 100 14757 a.out\r\n";
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

	public byte[] getData(){
		return data;
	}
	public int getDataSize(){
		return dataSize;
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

	public synchronized boolean remove(Node n){
		if(nextNodes.contains(n) == true){
			return nextNodes.remove(n);
		}else{
			System.out.println("No such node in this directory");
			return nextNodes.remove(n);
		}

	}



}