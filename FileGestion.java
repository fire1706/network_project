//---------------------------------FileGestion---------------------------------------------
//
// This function has for purposes to handle the management of the files/directories and
// the different types of connection on IPV4 and IPV6 in passive and active mode.
//
//Copyright (c) 2019 by Thomas BASTIN & Victor Dachet. All Rights Reserved.
//-----------------------------------------------------------------------------------------


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
	private InetAddress hostId = null;


	//Constructor
	FileGestion(Socket socketClient,/* int authorized,*/ FileVirtuel file_virtuel, int authorized){
		this.socketClient = socketClient;
		this.authorized = authorized;
		this.currentNode = file_virtuel.root;
	}


	public void menu(){
		String str = new String();
		try{
			InputStream inStream = socketClient.getInputStream();
      		OutputStream outStream = socketClient.getOutputStream();

    		BufferedReader input = new BufferedReader(new InputStreamReader(inStream));
      		String inString = new String();
      		String path = new String();
      		Boolean isClosed = false;
      		Boolean isChanged = false;
      		boolean previousRNFR = false;
      		Node nodeToChangeName = null;

		    while(true){
		    	System.out.println("Boucle du menu FileGestion");
				
		    	inString = input.readLine();
				System.out.println("Commande recue " + inString);

				/* --------- PWD -----------*/
				if(inString.equals("PWD")){
					path = "257 "+currentNode.getPath()+" \r\n";
					outStream.write(path.getBytes());

				/* --------- LIST -----------*/
				}else if(inString.equals("LIST")){
					if(dataChannel == null || data == null){// verify the data connection
						outStream.write("426 Connection closed; transfer aborted\r\n".getBytes());
						 
					}else{
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
							toSend = contentOfCurrentNode[i] + "\r\n";
							dataChannel.write(toSend.getBytes());
						}

						try{
							dataChannel.close();//closes the connection prevents the EOF to the client
							data.close();
						}catch(IOException e){
							e.printStackTrace();
						}
						outStream.write("226 entire directory was successfully transmitted\r\n".getBytes());
					}

						/* --------- PASSIVE -----------*/
				}else if(inString.equals("PASV")){
					Random randNumber = new Random();
					int firstnum = randNumber.nextInt(156) +100;
					int secondnum = randNumber.nextInt(256);

					InetAddress addressIP = socketClient.getLocalAddress();
					String host = InetAddress.getLocalHost().getHostAddress();
					int port = firstnum*256+secondnum;
					host = host.replace(".",",");

					String message = new String("227 Entering Passive Mode ("+host+","+firstnum+","+secondnum+")\r\n");
	    			outStream.write(message.getBytes());

					passiveSocket = new ServerSocket(port);
					try{
						data = passiveSocket.accept();//wait for the connection from the client
					  	dataChannel = data.getOutputStream();//To further send data
					  	dataChannelIN = data.getInputStream();//to further reiceive data
					}catch(IOException e){
						outStream.write("425 Can't open data connection.\r\n".getBytes());
					}

		        /* --------- EPSV -----------*/
		        }else if(inString.equals("EPSV")){
		        	Random randNumber = new Random();
		      		int firstnum = randNumber.nextInt(156) +100;
		      		int secondnum = randNumber.nextInt(256);

		      		InetAddress addressIP = socketClient.getLocalAddress();
		      		String host = InetAddress.getLocalHost().getHostAddress();
					int port = firstnum*256+secondnum;
					host = host.replace(".",",");

					String message = new String("229 Starting Extended Passive Mode (|||"+port+"|)\r\n");
					
					passiveSocket = new ServerSocket(port, 1, addressIP);
					outStream.write(message.getBytes());
					try{
						data = passiveSocket.accept();//wait for the connection from the client
					  	dataChannel = data.getOutputStream();//To further send data
					  	dataChannelIN = data.getInputStream();//to further reiceive data
					}catch(IOException e){
						outStream.write("425 Can't open data connection.\r\n".getBytes());
					  	e.printStackTrace();
					}

		        }else if(inString.startsWith("EPRT")){
		        	if(inString.length() < 6){
		        		outStream.write("501 error in arguments\r\n".getBytes());
		        	}else{//else1
		        		//due to function split which splits all chararcter pf the string with "|"
		        		str = inString.replace("|",",");
		        		String[] tabstr = str.split(",");
		        		if(tabstr.length != 4){
		        			outStream.write("501 error in arguments\r\n".getBytes());
		        		}else{//else2
							String getAddr = tabstr[2];
							String portS = tabstr[3];
							int port = Integer.valueOf(portS);
							
							try{
								data = new Socket(getAddr,port);
								dataChannel = data.getOutputStream();
								dataChannelIN = data.getInputStream();
								outStream.write("200 active extended\r\n".getBytes());
				        	}catch(IOException e){
								outStream.write("425	Can't open data connection.\r\n".getBytes());
				        		e.printStackTrace();
				        	}//end try and catch
		        		}//end else2
		        	}//end else1

				}else if(inString.startsWith("PORT")){
					if(inString.length() < 6){
						outStream.write("501 error in arguments\r\n".getBytes());
					}else{
						str = inString.substring(5);
						String[] tabstr = str.split(",");
						if(tabstr.length != 6){
							outStream.write("501 error in arguments\r\n".getBytes());
						}else{
							int p1 = Integer.valueOf(tabstr[4]);
							int p2 = Integer.valueOf(tabstr[5]);
							String getAddr = tabstr[0] + "." + tabstr[1] + "." + tabstr[2]+ "." + tabstr[3];
							int port = p1*256 +p2;

							try{
								InetAddress adressSocketActive = InetAddress.getByName(getAddr);
								data = null;
								while(data == null){
									data = new Socket(getAddr,port);
								}
				        		dataChannel = data.getOutputStream();
				        		dataChannelIN = data.getInputStream();
				        		outStream.write("200 active\r\n".getBytes());
								//outStream.write("225	Data connection open; no transfer in progress\r\n".getBytes());
				        	}catch(IOException e){
								outStream.write("425	Can't open data connection.\r\n".getBytes());
				        		e.printStackTrace();
				        	}catch(IllegalArgumentException e){
				        		outStream.write("425	Can't open data connection.\r\n".getBytes());
				        		e.printStackTrace();
				        	}catch(SecurityException e){
				        		outStream.write("425	Can't open data connection.\r\n".getBytes());
				        		e.printStackTrace();
				        	}catch(NullPointerException e){
				        		outStream.write("425	Can't open data connection.\r\n".getBytes());
				        		e.printStackTrace();
				        	}
						}
					}

				}else if( inString.equals("FEAT")){
				  	outStream.write("211 CWD CDUP DELE EPRT EPSV LIST MDTM MKD PASV PORT PWD QUIT RETR RMD RNFR RNTO STOR SYST\r\n".getBytes());

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
					Node n = null;
					if(inString.length() <5){
						outStream.write("501 error in arguments\r\n".getBytes());
					}else{
						path = inString.substring(4);
						List<Node> nextnodes = currentNode.getNextNodes();
						if(currentNode.getName().equals(path) || currentNode.getName().equals(path)){//saty in the current
							str = "250 directory changed to " + currentNode.getPath() + "\r\n";
							outStream.write(str.getBytes());
							isChanged = true;
						}else if(currentNode.getParent() != null){//permits to go back
							n = currentNode.getParent();
							if(path.equals(n.getPath()) || path.equals(n.getName())){
								currentNode = n;
								str = "250 directory changed to " + currentNode.getPath() + "\r\n";
								outStream.write(str.getBytes());
								isChanged = true;
							}
						}
						if(nextnodes == null){
							outStream.write("550 Requested action not taken. Directory unavailable from here\r\n".getBytes());
						}else{
							Object[] array = nextnodes.toArray();
							int size = nextnodes.size();
							String message = null;

							for(int i = 0; i<size ; i++){
								n = (Node) array[i];
								//attention plus tard changer inString par path
								if(path.equals(n.getName()) || path.equals(n.getPath())){
									if(n.getAuthorized() == 0){//everybody has access
										currentNode = n;
										str = "250 directory changed to " + currentNode.getPath() + "\r\n";
										outStream.write(str.getBytes());
										isChanged = true;
										break;
									}else if(n.getAuthorized() == 1 && n.getAuthorized() == authorized){//only Sam has accessed
										currentNode = n;
										str = "250 directory changed to " + currentNode.getPath() + "\r\n";
										outStream.write(str.getBytes());
										isChanged = true;
										break;
									}else{
										outStream.write("550 No access to this file/directory\r\n".getBytes());
									}
								}
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
					try{
						if(inString.length() < 5){
							outStream.write("501 error in arguments\r\n".getBytes());
						}else{
							path = inString.substring(4);
							List<Node> nextnodes = currentNode.getNextNodes();
							Object[] array = nextnodes.toArray();
							Node n = null;
							int size = nextnodes.size();
							boolean isRemoved = false;
							for(int i = 0; i<size; i++){
								n = (Node) array[i];
								if(path.equals(n.getName()) || path.equals(n.getPath())){
									if(authorized == 0 && n.getAuthorized() == 1){//access denied
										isRemoved = false;
										break;
									}else{
										if(n.isDirectory()){
											isRemoved = currentNode.remove(n);
										}
									}
								}
							}
							if(isRemoved == true){
								outStream.write("250 directory was successfully removed\r\n".getBytes());
							}else{
								outStream.write("550 not Okay \r\n".getBytes());
							}

						}
					}catch(Exception e){
						outStream.write("550 not Okay \r\n".getBytes());
					}
				/* --------- DELE -----------*/	
				}else if(inString.startsWith("DELE")){
					try{
						if(inString.length() < 6){
							outStream.write("501 error in arguments\r\n".getBytes());
						}else{
							path = inString.substring(5);
							List<Node> nextnodes = currentNode.getNextNodes();
							Object[] array = nextnodes.toArray();
							Node n = null;
							int size = nextnodes.size();
							boolean isRemoved = false;
							for(int i = 0; i<size; i++){
								n = (Node) array[i];
								if(path.equals(n.getName()) || path.equals(n.getPath())){
									if(authorized == 0 && n.getAuthorized() == 1){
										isRemoved = false;
										break;
									}else{
										if(n.isFile()){
											isRemoved = currentNode.remove(n);
										}
									}
								}
							}
							if(isRemoved == true){
								outStream.write("250 file was successfully removed\r\n".getBytes());
							}else{
								outStream.write("550 not Okay \r\n".getBytes());
							}
						}
					}catch(Exception e){
						outStream.write("550 not Okay \r\n".getBytes());
					}

				/* --------- MKD -----------*/
				}else if(inString.startsWith("MKD")){
					if(inString.length() < 5){
							outStream.write("501 error in arguments\r\n".getBytes());
						}else{
							path = inString.substring(4);
							List<Node> nextnodes = currentNode.getNextNodes();
							Object[] array = nextnodes.toArray();
							Node n = null;
							int size = nextnodes.size();
							boolean isPresent = false;

							for(int i = 0; i<size; i++){
								n = (Node) array[i];
								if(path.equals(n.getName()) || path.equals(n.getPath())){
									isPresent = true;
								}
							}

							if(isPresent == false){
								try{
									Node newNode = new Node(path, currentNode, 0);
									currentNode.addNextNode(newNode);
									str = "257 " + currentNode.getPath()+"\r\n";
									outStream.write(str.getBytes());
								}catch(NodeException e){
									e.printStackTrace();
									outStream.write("550 creation failed\r\n".getBytes());
								}
								
							}else{
								outStream.write("550 creation failed\r\n".getBytes());
							}
							
						}
				/* --------- TYPE -----------*/
				}else if( inString.startsWith("TYPE")){
					if(inString.length() != 6){
						outStream.write("501 Syntax error in parameters or arguments\r\n".getBytes());
					}
					str = inString.substring(inString.length() - 1);//last character of the line
	             	if(str.equals("I")){
	             		typeOfDataTransfer = 0;
	                	outStream.write("200 Type set to I\r\n".getBytes());
	              	}else if(str.equals("A")){
	              		typeOfDataTransfer = 1;
	                	outStream.write("200 Type set to A\r\n".getBytes());
	              	}else{
	              		outStream.write("501 Syntax error in parameters or arguments\r\n".getBytes());
	              	}

	            /* --------- RETR -----------*/
	            }else if(inString.startsWith("RETR")){
	            	if(dataChannel == null){
	            		outStream.write("426 Connection closed; transfer aborted\r\n".getBytes());
	            		isClosed = true;
	            	}else{
	            		isClosed = false;
	            		if(inString.length() < 6){
	            			outStream.write("501 error in arguments\r\n".getBytes());
		            	}else{
		            		path = inString.substring(5);
		            		List<Node> nextnodes = currentNode.getNextNodes();
							Object[] array = nextnodes.toArray();
							Node n = null;
							int size = nextnodes.size();
							byte[] dataToSend = null;

							for(int i = 0; i<size ; i++){
								n = (Node) array[i];
								if(path.equals(n.getName()) || path.equals(n.getPath())){
									dataToSend = n.getData();
									
									try{
										dataChannel.write(dataToSend);
										dataChannel.close();
										isClosed = true;
										outStream.write("226 the entire file was successfully written\r\n".getBytes());
									}catch(IOException e){
										System.out.println("Connection closed unwell after retr");
									}
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
		        /* --------- STOR -----------*/
	            }else if(inString.startsWith("STOR")){
	            	if(dataChannel == null){
	            		outStream.write("426 Connection closed; transfer aborted\r\n".getBytes());
	            		isClosed = true;
	            	}else{//else1
	            		isClosed = false;
	            		if(inString.length() < 6){
	            			outStream.write("501 error in arguments\r\n".getBytes());
		            	}else{//else 2
		            		path = inString.substring(5);
		            		ArrayList<Byte> receiveBytes = new ArrayList<Byte>();
							int size = 0;
							Byte dataToReceive ;
							BufferedReader dataChannelINReader = null;
							int reiceived = -1;
							
							List<Node> nextnodes = currentNode.getNextNodes();
							Object[] arrayNodes = nextnodes.toArray();
							Node n = null;
							int sizeNextnodes = nextnodes.size();
							boolean isAlreadyThere = false;

							for(int i = 0; i<sizeNextnodes ; i++){
								n = (Node) arrayNodes[i];
								if(path.equals(n.getName()) || path.equals(n.getPath())){
									isAlreadyThere = true;	
								}	
							}

							if(isAlreadyThere){
								outStream.write("553 Requested action not taken. File name not allowed.\r\n".getBytes());
							}else{
								//transfer of data type UTF-8
								if(typeOfDataTransfer == 1){
									dataChannelINReader = new BufferedReader(new InputStreamReader(dataChannelIN, "UTF-8"));

									while((reiceived = dataChannelINReader.read()) != -1){//read until the EOF
										dataToReceive = (byte) reiceived;
										receiveBytes.add(dataToReceive);
									}

									Object[] array = receiveBytes.toArray();
									size = receiveBytes.size();
									byte[] constructor = new byte[size];
									for(int i =0; i<size; i++){
										constructor[i] = (byte) array[i];
									}
									try{
										Node newNode = new Node(path, constructor, currentNode, 0);
										currentNode.addNextNode(newNode);
										dataChannelINReader.close();
									}catch(NodeException e){
										e.printStackTrace();
										System.out.println("Problem with creation of a new node to store the data reiceived");
									}catch(IOException e){
										e.printStackTrace();
									}
									outStream.write("226 entire file was successfully received and stored\r\n".getBytes());
								}else{//type of data transfer byte

									while((reiceived = dataChannelIN.read()) != -1){//read until the EOF
										dataToReceive = (byte) reiceived;
										receiveBytes.add(dataToReceive);
									}

									Object[] array = receiveBytes.toArray();
									size = receiveBytes.size();
									byte[] constructor = new byte[size];
									for(int i =0; i<size; i++){
										constructor[i] = (byte) array[i];
									}
									try{
										Node newNode = new Node(path, constructor, currentNode, 0);
										currentNode.addNextNode(newNode);
										dataChannelIN.close();
									}catch(NodeException e){
										e.printStackTrace();
										System.out.println("Problem with creation of a new node to store the data reiceived");
									}catch(IOException e){
										e.printStackTrace();
									}
									outStream.write("226 entire file was successfully received and stored\r\n".getBytes());
									

								}//end else 2
							}

							
						}
							
					}//end else1
				/* --------- QUIT -----------*/
	            }else if(inString.equals("QUIT")){
	            	outStream.write("221 bye\r\n".getBytes());

	            	try{
		            	outStream.close();
		            	inStream.close();
		            	if(socketClient != null){
		            		socketClient.close();
		            	}
		            	if(dataChannel != null){
		            		dataChannel.close();
		            	}
		            	if(dataChannelIN != null){
		            		dataChannelIN.close();
		            	}
		            	if(passiveSocket != null){
		            		passiveSocket.close();
		            	}
		            	if(activeSocket != null){
		            		activeSocket.close();
		            	}
		            	if(data!= null){
		            		data.close();
		            	}
	            	}catch(IOException e){
	            		e.printStackTrace();
	            	}
	            	break;
	            /* --------- MDTM -----------*/
	            }else if(inString.startsWith("MDTM")){
	            	if(inString.length() < 6){
	            		outStream.write("501 error in arguments\r\n".getBytes());
	            	}else{
	            		str = inString.substring(5);
	            		List<Node> nextnodes = currentNode.getNextNodes();
						Object[] array = nextnodes.toArray();
						Node n = null;
						int size = nextnodes.size();
						byte[] dataToSend = null;
						String message = null;
						boolean isThere = false;

						for(int i = 0; i<size ; i++){
							n = (Node) array[i];
							if(str.equals(n.getName()) || str.equals(n.getPath())){
								message = "213 "+n.getDate() + "\r\n";
								isThere = true;
								break;
							}	
						}
						if(isThere){
							outStream.write(message.getBytes());
						}else{
							outStream.write("501 Syntax error in parameters or arguments\r\n".getBytes());
						}
	            	}
	            /* --------- RNFR -----------*/
	            }else if(inString.startsWith("RNFR")){
	            	if(inString.length() < 6){
	            		outStream.write("501 error in arguments\r\n".getBytes());
	            	}else{

	            		str = inString.substring(5);
	            		List<Node> nextnodes = currentNode.getNextNodes();
						Object[] array = nextnodes.toArray();
						Node n = null;
						int size = nextnodes.size();
						byte[] dataToSend = null;
						String message = null;
						boolean isThere = false;


						for(int i = 0; i<size ; i++){
							n = (Node) array[i];
							if(str.equals(n.getName()) || str.equals(n.getPath())){
								message = "350 File exists, ready for destination name\r\n";
								if(authorized == 0 && n.getAuthorized() == 1){
									isThere = false;
									nodeToChangeName = null;
								}else{
									nodeToChangeName = n;
									previousRNFR = true;
									isThere = true;
									break;
								}
								
							}	
						}
						if(isThere){
							outStream.write(message.getBytes());
						}else{
							outStream.write("550 Requested action not taken. File unavailable\r\n".getBytes());
						}
	            	}
	            /* --------- RNTO -----------*/
	            }else if(inString.startsWith("RNTO")){
	            	if(inString.length() < 6){
	            		outStream.write("501 error in arguments\r\n".getBytes());
	            	}else{
	            		str = inString.substring(5);
	            		if(nodeToChangeName != null && previousRNFR == true){
	            			isChanged = nodeToChangeName.changeName(str);
	            		}
	            		if(isChanged){
	            			outStream.write("250 the file was renamed successfully\r\n".getBytes());
	            		}else{
	            			outStream.write("553 Requested action not taken. File name not allowed.\r\n".getBytes());
	            		}
	            	}
	            	previousRNFR = false;
	            	nodeToChangeName = null;
	            }else{
					outStream.write("502 command not implemented\r\n".getBytes());

				}// END of the commands


      		}//END of While

		}catch(UnknownHostException e){
			System.out.println("Error of host");
			e.printStackTrace();
		}catch(IOException e){
			System.out.println("Problem in input and output streams");
			e.printStackTrace();
		}catch(SecurityException e){
			e.printStackTrace();
		}catch(IllegalArgumentException e){
			e.printStackTrace();
		}catch(NullPointerException e){
			System.out.println("Error of reading in the inputstream");
			//e.printStackTrace();
		}//END of try and catch

	}//END of menu
}//END of class
