//---------------------------------FileVirtuel---------------------------------------------
//
// This object has for purpose to create virtual files like in the assignment.
//
//Copyright (c) 2019 by Thomas BASTIN & Victor Dachet. All Rights Reserved.
//-----------------------------------------------------------------------------------------

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

		}catch(NodeException e){
			e.printStackTrace();
		}
	}
}
