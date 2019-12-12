// grosse fonction comprenant tout les fonctins de gestion des fichiers
// Downlmoads (RETR)
// Uploads (STOR)
// SYST ok
// FEAT
// MDTM
// rename
// delete
// navigate into directory
// ls toi même tu sais

// not mandatiry but very help fuuuuuull mageul
// CDUP
// CWD
// LIST ok
// PWD ok
//

import java.net.*;
import java.io.*;
import java.util.*;

public class FileGestion{
	int authorized = -1;
	Socket socketClient = null;
	Node currentNode = null;
	ServerSocket passiveSocket = null;
	Socket activeSocket = null;
	Socket data = null;
	OutputStream dataChannel = null;
	int port = -1;
	InetAddress hostI = null;

	//Constructor
	FileGestion(Socket socketClient,/* int authorized,*/ FileVirtuel file_virtuel){
		this.socketClient = socketClient;
		//this.authorized = authorized;
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
          	String path = new String();
          	while(true){
				inString = input.readLine();
				System.out.println("boucle du menu FileGestion");
				System.out.println("Commande recue" + inString);

				if(inString.contains("PWD")){
					outStream.write("257 /\r\n".getBytes());
				/* --------- LIST -----------*/
				}else if(inString.contains("LIST")){
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
						toSend = contentOfCurrentNode[i] + "\r\n";
						outStream.write(toSend.getBytes());
					}
					//décommenter ligne suivante pour test fin d'envoi
					//outStream.write("212 End of list\r\n".getBytes());
					System.out.println("Content must be send to Client");



					// ici je pense pas que se sois la commande 110 vic Bien vu Thom MERCI!!
				/* --------- PASSIVE -----------*/
				}else if(inString.contains("PASV")){
					Random randNumber = new Random();
					int firstnum = randNumber.nextInt(156) +100;
					int secondnum = randNumber.nextInt(256);

					InetAddress addressIP = socketClient.getLocalAddress();
					hostId = addressIP.getLocalHost();
					String host = hostId.getHostAddress();
					port = firstnum*256+secondnum;
					host = host.replace(".",",");

					String message = new String("227 Entering Passive Mode("+host+","+firstnum+","+secondnum+")\r\n");
        			outStream.write(message.getBytes());

        			/*passiveSocket = new ServerSocket(firstnum * 256 + secondnum);
        			Socket data = passiveSocket.accept();
        			try{
        				dataChannel = data.getOutputStream();
        			}catch(IOException e){
        				e.printStackTrace();
        			}*/

        		/* --------- EPSV -----------*/
        		}else if(inString.contains("EPSV")){
        			Random randNumber = new Random();
      				int firstnum = randNumber.nextInt(156) +100;
      				int secondnum = randNumber.nextInt(256);

      				InetAddress addressIP = socketClient.getLocalAddress();
      				hostId = addressIP.getLocalHost();
      				String host = hostId.getHostAddress();
							port = firstnum*256+secondnum;
							host = host.replace(".",",");

							String message = new String("229 Entering Passive Mode("+host+","+firstnum+","+secondnum+")\r\n");
        			outStream.write(message.getBytes());

        			//passiveSocket = new ServerSocket(firstnum * 256 + secondnum);
        			/*Socket data = passiveSocket.accept();
        			try{
        				dataChannel = data.getOutputStream();

        			}catch(IOException e){
        				e.printStackTrace();
        			}*/

        		}else if(inString.contains("EPRT") || inString.contains("PORT")){
							int n2 = inString.length() - 7 ;
							String getAddr = inString.substring(8,n2);
							System.out.println(getAddr);
							data = new Socket(getAddr,1025,hostI,port);

							try{
        				dataChannel = data.getOutputStream();

        			}catch(IOException e){
        				e.printStackTrace();
        			}

        			outStream.write("200 \r\n".getBytes());


				/* --------- SYST -----------*/
				}else if(inString.contains("SYST")){
					Properties prop = new Properties();
					prop = System.getProperties();
					str = prop.getProperty("os.arch");
					outStream.write("215\r\n".getBytes());
				/* --------- HELP -----------*/
				}else if(inString.contains("HELP")){

				/* --------- null -----------*/
				}else if(inString == null){
					outStream.write("500\r\n".getBytes());

				/* --------- CWD -----------*/
				}else if(inString.contains("CWD")){
					path = inString.substring(4);
					List<Node> nextnodes = currentNode.getNextNodes();

					//inString.
				/* --------- TYPE -----------*/
				}else if( inString.contains("TYPE")){
                  if(inString.contains("I")){
                    outStream.write("200 Type set to I\r\n".getBytes());
                  }
                  if(inString.contains("A")){
                    outStream.write("200 Type set to A\r\n".getBytes());
                  }
                  //inString = inStream.readLine();
                /* --------- DEFAULT -----------*/
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
