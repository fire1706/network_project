import java.util.*;

public class arbre{

	
	public static void main(String[] args){
		try{
			Node root = new Node("/", true);
			Node privateFolder = new Node("private", root);
			root.addNextNode(privateFolder);

			Node secret = new Node("secret.txt", "UPUPDOWNDOWNLEFTRIGHTLEFTRIGHTBASTART".getBytes(), privateFolder);
			privateFolder.addNextNode(secret);

			Node mytext = new Node("mytext.txt", "Irasshaimase".getBytes(), root);
			root.addNextNode(mytext);

			byte[] myImg = {66,  77,  70,  1,  0,  0,    0,   0,   0,   0,  62,   0,   0,  0,   40,   0,
                        	 0,   0,  34,  0,  0,  0,   33,   0,   0,   0,   1,   0,   1,  0,    0,   0,
                        	 0,   0,   8,  1,  0,  0,    0,   0,   0,   0,   0,   0,   0,  0,    0,   0,
                        	 0,   0,   0,  0,  0,  0,    0,   0,   0,   0,  -1,  -1,  -1,  0,   -1,  -1,
                       		 -1,  -1, -64,  0,  0,  0,   -1, -32,   0,   1, -64,   0,   0,  0, -128,  31,
                        	-1,  -2, -64,  0,  0,  0,  -65, -33,  -1,  -2, -64,   0,   0,  0,  -65, -33,
                        	-1,  -2, -64,  0,  0,  0,  -65, -33,  -1,  -8, -64,   0,   0,  0,  -65, -33,
                        	-1,  -2, -64,  0,  0,  0,  -65, -33,  -1,  -2, -64,   0,   0,  0,  -65, -33,
                        	-1,  -2, -64,  0,  0,  0,  -65, -33,  -1,  -2, -64,   0,   0,  0,  -65, -33,
                    	    -1,  -2, -64,  0,  0,  0,  -65, -33,  -1,  -8, -64,   0,   0,  0,  -65, -33,
                    	    -1,  -2, -64,  0,  0,  0,  -65, -33,  -1,  -2, -64,   0,   0,  0,  -65, -33,
                    	    -1,  -2, -64,  0,  0,  0,  -65, -33,  -1,  -2, -64,   0,   0,  0,  -65, -33,
                    	    -1,  -2, -64,  0,  0,  0, -128,  31,  -1,  -8, -64,   0,   0,  0,   -1, -17,
                	        -1,  -2, -64,  0,  0,  0,   -1,  -9,  -1,  -2, -64,   0,   0,  0,   -1,  -5,
                    	    -1,  -2, -64,  0,  0,  0,   -1,  -3,  -2,   0, -64,   0,   0,  0,   -1,  -2,
                	        -3,  -1, -64,  0,  0,  0,   -1,  -1, 126,  -1, -64,   0,   0,  0,   -1,  -1,
            	           127, 127, -64,  0,  0,  0,   -1,  -1, -65, 127, -64,   0,   0,  0,   -1,  -1,
        	               -33, -65, -64,  0,  0,  0,   -1,  -1, -17, -65, -64,   0,   0,  0,   -1,  -1,
                	       -17, -33, -64,  0,  0,  0,   -1,  -1,  -9, -33, -64,   0,   0,  0,   -1,  -1,
            	            -9, -33, -64,  0,  0,  0,   -1,  -1,  -8,  31, -64,   0,   0,  0,   -1,  -1,
        	                -1,  -1, -64,  0,  0,  0
                        
        	};

			Node myimage = new Node("myimage.bmp", myImg, root);
			root.addNextNode(myimage);

			int size = root.getSizeContent();

			String[] str = new String[size];
			str = root.getDirectoryContent();
			for(int i=0; i<size; i++){
				System.out.println(str[i]);
			}

			System.out.println(secret.getRepository());

			
			// String str2 = new String("");
			// Node n = privateFolder;
			// while(n != null){
			// 	//System.out.println(n.getName());
			// 	str2 = n.getName() + "/";
			// 	//System.out.println(str2);
			// 	n = n.getParent();
			// }
			
				

			//System.out.println(str2);
			

		}catch(NodeException e){
			e.printStackTrace();
		}








	}

	// public static String getRepository(Node node){
	// 	String str2 = new String("");
	// 	Node n = node;

	// 		while(n.getParent() != null){
	// 			//System.out.println(n.getName());
	// 			str2 = n.getName() + "/";
	// 			//System.out.println(str2);
	// 			n = n.getParent();
	// 		}
	// 		str2 = "/" + str2;
	// 		return str2;

	// }



}