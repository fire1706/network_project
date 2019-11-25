// grosse fonction comprenant tout les fonctins de gestion des fichiers
// Downlmoads
// Uploads
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

public class FileGestion{
	int authorized = -1;
	Socket socketClient = null;
	FileGestion(Socket socketClient, int authorized){
		this.socketClient = socketClient;
		this.authorized = authorized;
	}

	public void menu(){
		try{
			System.out.println("Welcome in the menu:");
			System.out.println("Send HELP if you need some help.");

			InputStream inStream = socketClient.getInputStream();
          	OutputStream outStream = socketClient.getOutputStream();

          	BufferedReader input = new BufferedReader(new InputStreamReader(inStream));
          	String inString = input.readLine();
		}catch(UnknownHostException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		


	}
}