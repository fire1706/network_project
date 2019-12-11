import java.util.*;

public class Node{
	private List<Node> nextNodes = new ArrayList<Node>();
	private Node parent = null;
	private String name = null;
	private boolean isDirectory = false;
	private boolean isFile = false;
	private boolean isRoot = false;
	private byte[] data = null;

	Node(){}

	Node(String name){
		this.name = name;
		this.isDirectory = true;

	}

	Node(String name, boolean isRoot){
		this.name = name;
		this.isDirectory = true;
		this.isRoot = isRoot;

	}


	Node(String name, Node parent) throws NodeException{
		if(parent.isDirectory() == false)
			throw new NodeException("parent is not a directory");
		this.name = name;
		this.isDirectory = true;
		this.parent = parent;

	}

	Node(String name, byte[] data, Node parent) throws NodeException{
		if(parent.isDirectory() == false)
			throw new NodeException("parent is not a directory");
		this.name = name;
		this.data = data;
		this.isFile = true;
		this.parent = parent;
	}

	public List<Node> getNextNodes(){
		return nextNodes;
	}

	public Node getParent(){
		return parent;
	}

	public String getName(){
		return name;
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

	public byte[] getData(){
		return data;
	}

	public void addNextNode(Node nextNode){
		nextNodes.add(nextNode);
	}

	public boolean removeNode(Node removeNode){
		return nextNodes.remove((Node) removeNode);
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
				str[i] = n.getName();
			}
			return str;
		}
	}

	public int getSizeContent(){
		if(isDirectory == false)
			return 0;
		return nextNodes.size();
	}

	public String getRepository(){
		String str2 = new String("");
		String str1 = null;
		Node n = parent;

			while(n != null){
				System.out.println(str2);
				System.out.println("Hello mon parent s appelle:" + n.getName());
				str1 = n.getName();
				System.out.println(str1);
				str2 = str2 + "/" + str1;
				System.out.println(str2);
				//System.out.println(str2);
				n = n.getParent();
				
			}
			str2 = "/" + str2;
			return str2;

	}

	







}