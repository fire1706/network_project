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
	private int authorized = -1; //si Sam =1   si anonymous = 0
	private int typeOfDataTransfer = 0; // =0 byte transfer =1 UTF8 transfer
	private Socket socketClient = null;
	private Node currentNode = null;
	private ServerSocket passiveSocket = null;
	private Socket activeSocket = null;
	private Socket data = null;
	private OutputStream dataChannel = null;
	private InputStream dataChannelIN = null;


	//Constructor
	FileGestion(Socket socketClient,/* int authorized,*/ FileVirtuel file_virtuel, int authorized){
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
      		String path = new String();
      		Boolean isClosed = false;
      		Boolean isChanged = false;

	    while(true){
			while((inString = input.readLine())== null){}// pour pouvoir attendre la commande et pas recevoir un null et avoir une exception
			System.out.println("boucle du menu FileGestion");

			System.out.println("Commande recue " + inString);

			/* --------- PWD -----------*/
			if(inString.equals("PWD")){
				path = "257 "+currentNode.getPath()+" \r\n";
				outStream.write(path.getBytes());

			/* --------- AUTH -----------*/
			}else if(inString.contains("AUTH")){
				outStream.write("502 command not supported\r\n".getBytes());

			/* --------- LIST -----------*/
			}else if(inString.contains("LIST")){
				System.out.println("ici");
				if(dataChannel == null){
					outStream.write("426 Connection closed; transfer aborted\r\n".getBytes());
					 
				}else{
					//outStream.write("213 list comes:\r\n".getBytes());
					int sizeOfCurrentNode = currentNode.getSizeContent();
					String[] contentOfCurrentNode = new String[sizeOfCurrentNode];
					String toSend = null;
					try{
						contentOfCurrentNode = currentNode.getDirectoryContent();
					}catch(NodeException e){
						contentOfCurrentNode = null;
					}
					outStream.write("125 Data connection opened\r\n".getBytes());
					for(int i = 0; i<sizeOfCurrentNode; i++){
						toSend = contentOfCurrentNode[i] + "\r\n";// regarder MLSD facon d'envoyer les truc
						dataChannel.write(toSend.getBytes());
					}
					//outStream.write("226 entire directory was successfully transmitted\r\n".getBytes());
					//ligne suivante absolument nécessaire!!!!!!!!!
					try{
						dataChannel.close();
						data.close();
					}catch(IOException e){
						e.printStackTrace();
					}
					
					outStream.write("226 entire directory was successfully transmitted\r\n".getBytes());

					

					//décommenter ligne suivante pour test fin d'envoi

					System.out.println("Content must be sent to Client");
				}

						// ici je pense pas que se sois la commande 110 vic Bien vu Thom MERCI!!
					/* --------- PASSIVE -----------*/
			}else if(inString.equals("PASV")){
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
				//outStream.write("150	File status okay; about to open data connection\r\n".getBytes());

				passiveSocket = new ServerSocket(port);
				try{
					data = passiveSocket.accept();
				  	dataChannel = data.getOutputStream();
				  	dataChannelIN = data.getInputStream();
					//outStream.write("225	Data connection open; no transfer in progress\r\n".getBytes());
				}catch(IOException e){
					outStream.write("425	Can't open data connection.\r\n".getBytes());
				  	e.printStackTrace();
				}

	        /* --------- EPSV -----------*/
	        }else if(inString.equals("EPSV")){
	        	Random randNumber = new Random();
	      		int firstnum = randNumber.nextInt(156) +100;
	      		int secondnum = randNumber.nextInt(256);

	      		InetAddress addressIP = socketClient.getLocalAddress();
	      		InetAddress hostId = addressIP.getLocalHost();
	      		String host = hostId.getHostAddress();
				int port = firstnum*256+secondnum;
				host = host.replace(".",",");

				String message = new String("229 Starting Extended Passive Mode (|||"+port+"|)\r\n");
				
				//outStream.write("150	File status okay; about to open data connection\r\n".getBytes());

				System.out.println("Instanciation nouveau ServerSocket pour dataChannel");
				passiveSocket = new ServerSocket(port, 1, addressIP);
				outStream.write(message.getBytes());
				try{
					data = passiveSocket.accept();
				  	dataChannel = data.getOutputStream();
				  	dataChannelIN = data.getInputStream();
				  	System.out.println("Client connecté à la dataChannel");
					//outStream.write("225	Data connection open; no transfer in progress\r\n".getBytes());
				}catch(IOException e){
					outStream.write("425	Can't open data connection.\r\n".getBytes());
				  	e.printStackTrace();
				}

	        }else if(inString.startsWith("EPRT")){
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
					dataChannelIN = data.getInputStream();
									//outStream.write("225	Data connection open; no transfer in progress\r\n".getBytes());
	        	}catch(IOException e){
					outStream.write("425	Can't open data connection.\r\n".getBytes());
	        		e.printStackTrace();
	        	}



			}else if(inString.startsWith("PORT")){

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
	        		dataChannelIN = data.getInputStream();
					//outStream.write("225	Data connection open; no transfer in progress\r\n".getBytes());
	        	}catch(IOException e){
					outStream.write("425	Can't open data connection.\r\n".getBytes());
	        		e.printStackTrace();
	        	}


			}else if( inString.equals("FEAT")){
			  	outStream.write("211 CWD CDUP PASV PWD RETR SYST\r\n".getBytes());



			/* --------- SYST -----------*/
			}else if(inString.equals("SYST")){
				Properties prop = new Properties();
				prop = System.getProperties();
				str = prop.getProperty("os.arch");
				String mes = "215 "+str+" \r\n";
				outStream.write(mes.getBytes());
			/* --------- HELP -----------*/
			}else if(inString.equals("HELP")){
				outStream.write("211 type FEAT to have the list of implemented commands\r\n".getBytes());

			/* --------- null -----------*/
			}else if(inString == null){
				outStream.write("500 \r\n".getBytes());

			/* --------- CWD -----------*/
			}else if(inString.startsWith("CWD")){

				if(inString.length() <5){
					outStream.write("501 error in arguments\r\n".getBytes());
				}else{
					path = inString.substring(4);
					List<Node> nextnodes = currentNode.getNextNodes();
					if(nextnodes == null){
						outStream.write("550 Requested action not taken. Directory unavailable from here\r\n".getBytes());
					}else{
						Object[] array = nextnodes.toArray();
						Node n = null;
						int size = nextnodes.size();
						String message = null;

						for(int i = 0; i<size ; i++){
							n = (Node) array[i];
							//attention plus tard changer inString par path
							if(path.contains(n.getName()) || path.contains(n.getPath())){
								this.currentNode = n;
								str = "250 directory changed to " + currentNode.getPath() + "\r\n";
								outStream.write(str.getBytes());
								isChanged = true;
								break;
							}else if(path.contains("\\") || path.contains(currentNode.getName())){
								str = "250 directory changed to " + currentNode.getPath() + "\r\n";
								outStream.write(str.getBytes());
								isChanged = true;
								break;
							}
						}
						if(isChanged != true){
							outStream.write("501 error in arguments\r\n".getBytes());
						}
					}
					
				}

						/* ---------CDUP-------------*/
			}else if(inString.equals("CDUP")){
				try{
					currentNode = currentNode.getParent();
					str = "250 okay new current directory "+currentNode.getName()+"\r\n";
					outStream.write(str.getBytes());
				}catch(Exception e){
					outStream.write("550 not Okay \r\n".getBytes());
				}


				/* ---------RMD-------------*/
			}else if(inString.startsWith("RMD")){
				try{//A terminer ca va pas du tout
					outStream.write("250 okay \r\n".getBytes());
				}catch(Exception e){
					outStream.write("550 not Okay \r\n".getBytes());
				}


			/* --------- TYPE -----------*/
			}else if( inString.startsWith("TYPE")){
             	if(inString.contains("I")){
             		typeOfDataTransfer = 0;
                	outStream.write("200 Type set to I\r\n".getBytes());
              	}
              	if(inString.contains("A")){
              		typeOfDataTransfer = 1;
                	outStream.write("200 Type set to A\r\n".getBytes());
              	}

            /* --------- RETR -----------*/
            }else if(inString.startsWith("RETR")){
            	if(dataChannel == null){
            		outStream.write("426 Connection closed; transfer aborted\r\n".getBytes());
            		isClosed = true;
            	}else{
            		isClosed = false;
            		if(inString.length() < 5){
            		outStream.write("501 error in arguments\r\n".getBytes());
	            	}else{
	            		path = inString.substring(4);
	            		List<Node> nextnodes = currentNode.getNextNodes();
						Object[] array = nextnodes.toArray();
						Node n = null;
						int size = nextnodes.size();
						byte[] dataToSend = null;

						for(int i = 0; i<size ; i++){
							n = (Node) array[i];
							//attention plus tard changer inString par path
							if(path.contains(n.getName()) || path.contains(n.getPath())){
								dataToSend = n.getData();
								
								try{
									dataChannel.write(dataToSend);
									dataChannel.close();
									isClosed = true;
									outStream.write("226 the entire file was successfully written\r\n".getBytes());
								}catch(IOException e){
									e.printStackTrace();
								}
								
								
								
								//outStream.write(str.getBytes());
								break;
							}
								
							}
						if(isClosed != true){
							try{
								outStream.write("501 error in arguments\r\n".getBytes());
							}catch(IOException e){
								e.printStackTrace();
							}
							
						}
					}
	            }
            }else if(inString.startsWith("STOR")){
            	if(dataChannel == null){
            		outStream.write("426 Connection closed; transfer aborted\r\n".getBytes());
            		isClosed = true;
            	}else{
            		isClosed = false;
            		if(inString.length() < 5){
            		outStream.write("501 error in arguments\r\n".getBytes());
	            	}else{
	            		path = inString.substring(4);
	            		List<Node> nextnodes = currentNode.getNextNodes();
						Object[] array = nextnodes.toArray();
						Node n = null;
						int size = nextnodes.size();
						byte[] dataToSend = null;

						for(int i = 0; i<size ; i++){
							n = (Node) array[i];
							//attention plus tard changer inString par path
							if(path.contains(n.getName()) || path.contains(n.getPath())){
								dataToSend = n.getData();
								
								try{
									dataChannel.write(dataToSend);
									dataChannel.close();
									isClosed = true;
									outStream.write("226 the entire file was successfully written\r\n".getBytes());
								}catch(IOException e){
									e.printStackTrace();
								}
								
								
								
								//outStream.write(str.getBytes());
								break;
							}
								
							}
						if(isClosed != true){
							try{
								outStream.write("501 error in arguments\r\n".getBytes());
							}catch(IOException e){
								e.printStackTrace();
							}
							
						}
					}
	            }
            }else{
				outStream.write("502 command not implemented\r\n".getBytes());

			}

      	}


		}catch(UnknownHostException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}



	}
}
