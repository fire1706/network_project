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
					path = "257 "+currentNode.getPath()+"\r\n";
					outStream.write(path.getBytes());

				}else if(inString.contains("AUTH")){
					outStream.write("502 command not supported\r\n".getBytes());



				/* --------- LIST -----------*/
				}else if(inString.contains("LIST")){
					System.out.println("ici");

					outStream.write("213 list comes:\r\n".getBytes());
					int sizeOfCurrentNode = currentNode.getSizeContent();
					String[] contentOfCurrentNode = new String[sizeOfCurrentNode];
					String toSend = null;
System.out.println("tak 1");
					try{
						contentOfCurrentNode = currentNode.getDirectoryContent();
					}catch(NodeException e){
						contentOfCurrentNode = null;
					}
System.out.println("tak 2");
outStream.write("125 Data connection already open; transfer starting\r\n".getBytes());
					for(int i = 0; i<sizeOfCurrentNode; i++){
						toSend = contentOfCurrentNode[i] + "\r\n";// regarder MLSD facon d'envoyer les truc
						dataChannel.write(toSend.getBytes());
					}
					outStream.write("226 Data connection gonna be closed\r\n".getBytes());
					//dataChannel.close();

					//data.close();

					//décommenter ligne suivante pour test fin d'envoi

					System.out.println("Content must be send to Client");



					// ici je pense pas que se sois la commande 110 vic Bien vu Thom MERCI!!
				/* --------- PASSIVE -----------*/
				}else if(inString.contains("PASV")){
					Random randNumber = new Random();
					int firstnum = randNumber.nextInt(156) +100;
					int secondnum = randNumber.nextInt(256);

					InetAddress addressIP = socketClient.getLocalAddress();
					InetAddress hostId = addressIP.getLocalHost();
					String host = hostId.getHostAddress();
					int port = firstnum*256+secondnum;
					host = host.replace(".",",");

					String message = new String("227 Entering Passive Mode ("+host+","+firstnum+","+secondnum+")\r\n");
        	outStream.write(message.getBytes());
					outStream.write("150	File status okay; about to open data connection\r\n".getBytes());
System.out.println("hrenajb");
					passiveSocket = new ServerSocket(port);
					try{
System.out.println("hrenajb");
						Socket data = passiveSocket.accept();
System.out.println("hrenajb");
					  dataChannel = data.getOutputStream();
						outStream.write("225	Data connection open; no transfer in progress\r\n".getBytes());
					}catch(IOException e){
						outStream.write("425	Can't open data connection.\r\n".getBytes());
					  e.printStackTrace();
					}

        /* --------- EPSV -----------*/
        }else if(inString.contains("EPSV")){
        	Random randNumber = new Random();
      		int firstnum = randNumber.nextInt(156) +100;
      		int secondnum = randNumber.nextInt(256);

      		InetAddress addressIP = socketClient.getLocalAddress();
      		InetAddress hostId = addressIP.getLocalHost();
      		String host = hostId.getHostAddress();
					int port = firstnum*256+secondnum;
					host = host.replace(".",",");

					String message = new String("229 Starting Extended Passive Mode (|||"+port+"|)\r\n");
   				outStream.write(message.getBytes());
					outStream.write("150	File status okay; about to open data connection\r\n".getBytes());

					System.out.println("here1");
					passiveSocket = new ServerSocket(port);
					System.out.println("here2");
					try{
						Socket data = passiveSocket.accept();

					  	dataChannel = data.getOutputStream();
						//outStream.write("225	Data connection open; no transfer in progress\r\n".getBytes());
					}catch(IOException e){
						outStream.write("425	Can't open data connection.\r\n".getBytes());
					  	e.printStackTrace();
					}

        		}else if(inString.contains("EPRT")){
	  					int n2 = inString.length() - 7 ;
							int n1 = inString.length() - 6 ;
							int n3 = inString.length() -1 ;
							String getAddr = inString.substring(8,n2);
							System.out.println(inString);
							System.out.println(getAddr);
							String portS = inString.substring(n1,n3);
							System.out.println(portS);
							int port =  Integer.valueOf(portS);
							outStream.write("200 \r\n".getBytes());
							try{
								outStream.write("150	File status okay; about to open data connection\r\n".getBytes());
								Socket data = new Socket(getAddr,port);
        				dataChannel = data.getOutputStream();
								outStream.write("225	Data connection open; no transfer in progress\r\n".getBytes());
        			}catch(IOException e){
								outStream.write("425	Can't open data connection.\r\n".getBytes());
        				e.printStackTrace();
        			}



						}else if(inString.contains("PORT")){

							int n1 = inString.length();
							int n2 = inString.length() - 7;
							int n3 = 5;
							int n4 = inString.length() - 8;
							String getAddr = inString.substring(n3,n4);
							getAddr = getAddr.replace(",",".");
System.out.println(getAddr);
							String portS = inString.substring(n2,n1);
	System.out.println(portS+"  "+portS.length());
							int p1 = Integer.valueOf(portS.substring(0,3));
							int p2 = Integer.valueOf(portS.substring(4,7));
System.out.println(p1+"  "+p2);
							int port = p1*256 +p2;

							outStream.write("200 \r\n".getBytes());
							try{
								outStream.write("150	File status okay; about to open data connection\r\n".getBytes());
								Socket data = new Socket(getAddr,port);
        				dataChannel = data.getOutputStream();
								outStream.write("225	Data connection open; no transfer in progress\r\n".getBytes());
        			}catch(IOException e){
								outStream.write("425	Can't open data connection.\r\n".getBytes());
        				e.printStackTrace();
        			}


						}else if( inString.contains("FEAT")){
						  outStream.write("211 to do \r\n".getBytes());



				/* --------- SYST -----------*/
				}else if(inString.contains("SYST")){
					Properties prop = new Properties();
					prop = System.getProperties();
					str = prop.getProperty("os.arch");
					String mes = "215 "+str+" \r\n";
					outStream.write(mes.getBytes());
				/* --------- HELP -----------*/
				}else if(inString.contains("HELP")){

				/* --------- null -----------*/
				}else if(inString == null){
					outStream.write("500 \r\n".getBytes());

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
					outStream.write("502 \r\n".getBytes());

				}

          	}


		}catch(UnknownHostException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}



	}
}
