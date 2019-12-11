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
	FileGestion(Socket socketClient, int authorized){
		this.socketClient = socketClient;
		this.authorized = authorized;
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

				if(inString.contains("PWD")){
					outStream.write("257 /\r\n".getBytes());
				}
				else if(inString.contains("LIST")){
					System.out.println("ici");
					outStream.write("110 private mytext.txt myimage.bmo\r\n".getBytes());// ici je pense pas que se sois la commande 110 vic
				}
				else if(inString.contains("SYST")){
					Properties prop = new Properties();
					prop = System.getProperties();
					str = prop.getProperty("os.arch");
					outStream.write("215 \r\n".getBytes());
				}
				else if(inString.contains("HELP")){

				}



          	}


		}catch(UnknownHostException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}



	}
}
