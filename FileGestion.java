// grosse fonction comprenant tout les fonctins de gestion des fichiers
// Downlmoads (RETR)
// Uploads (STOR)
// SYST
// FEAT
// MDTM
// rename
// delete
// navigate into directory
// ls toi même tu sais

// not mandatiry but very help fuuuuuull mageul
// CDUP
// CWD
// LIST
// PWD
//

import java.net.*;
import java.io.*;
import java.util.*;

public class FileGestion{
	int authorized = -1;
	Socket socketClient = null;
	Node currentNode = null;

	//Constructor
	FileGestion(Socket socketClient, int authorized, FileVirtuel file_virtuel){
		this.socketClient = socketClient;
		this.authorized = authorized;
		this.currentNode = file_virtuel.root;
	}


	public void menu(){
		String str = new String();
		try{
			System.out.println("Welcome in the menu:");
			System.out.println("Send HELP if you need some help.");

			InputStream inStream = socketClient.getInputStream();
          	OutputStream outStream = socketClient.getOutputStream();

          	BufferedReader input = new BufferedReader(new InputStreamReader(inStream));
          	String inString = new String();
          	while(true){
				inString = input.readLine();
				System.out.println("boucle du menu FileGestion");
				System.out.println("Commande recue" + inString);

				if(inString.contains("PWD")){
					outStream.write("257 /\r\n".getBytes());
				}
				else if(inString.contains("LIST")){
					System.out.println("ici");
					outStream.write("212 list comes:\r\n".getBytes());
					int sizeOfCurrentNode = currentNode.getSizeContent();
					String[] contentOfCurrentNode = new String[sizeOfCurrentNode];
					String toSend = null;
					try{
						contentOfCurrentNode = currentNode.getDirectoryContent();
					}catch(NodeException e){
						contentOfCurrentNode = null;
					}
					

					for(int i = 0; i<sizeOfCurrentNode; i++){
						toSend = "/" + contentOfCurrentNode[i] + "\r\n";
						outStream.write(toSend.getBytes());
					}
					//outStream.write("212 End of list\r\n".getBytes());
					System.out.println("Content must be send to Client");



					// ici je pense pas que se sois la commande 110 vic Bien vu Thom MERCI!!
				}
				else if(inString.contains("SYST")){
					Properties prop = new Properties();
					prop = System.getProperties();
					str = prop.getProperty("os.arch");
					outStream.write("215\r\n".getBytes());
				}
				else if(inString.contains("HELP")){

				}else if(inString == null){
					outStream.write("500\r\n".getBytes());

				}else{
					outStream.write("502\r\n".getBytes());

				}



          	}


		}catch(UnknownHostException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}



	}
}
