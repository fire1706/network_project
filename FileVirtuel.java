//Authors: Bastin Thomas & Dachet Victor

import java.util.*;
import java.lang.*;

public class FileVirtuel{
	Node root = null;

	
	public FileVirtuel(){
		try{
			root = new Node("/", true, 1);
			Node privateFolder = new Node("private", root, 1);
			root.addNextNode(privateFolder);

			Node secret = new Node("secret.txt", "UPUPDOWNDOWNLEFTRIGHTLEFTRIGHTBASTART".getBytes(), privateFolder, 1);
			privateFolder.addNextNode(secret);

			Node mytext = new Node("mytext.txt", "Irasshaimase".getBytes(), root, 0);
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

			Node myimage = new Node("myimage.bmp", myImg, root, 0);
			root.addNextNode(myimage);

			System.out.println(myimage.getPath());
			System.out.println(privateFolder.getPath());
			System.out.println(secret.getPath());
			
			System.out.println("----Date Format----");
			System.out.println(myimage.getDataFormalism());

			/* A ajouter si tu veux rajouter un dossier contenant un fichier dans private
			Node folder = new Node("folder", privateFolder);
			privateFolder.addNextNode(folder);

			Node text = new Node("text.txt", folder);
			folder.addNextNode(text);
			*/
			int size = root.getSizeContent();

			String[] str = new String[size];
			str = root.getDirectoryContent();
			for(int i=0; i<size; i++){
				System.out.println(str[i]);
			}
			//System.out.println(str2);
			

		}catch(NodeException e){
			e.printStackTrace();
		}


	}

	public String[] list(Node n){
		String[] str = null;
		try{


			int size = n.getSizeContent();

			str = new String[size];
			str = n.getDirectoryContent();
			for(int i=0; i<size; i++){
				System.out.println(str[i]);
			}
		}catch(NodeException e){
			e.printStackTrace();
		}
		return str;
	}

	



}