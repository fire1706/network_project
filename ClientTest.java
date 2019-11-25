// ClientTest is a fake client to test our server and FTP protocol

import java.net.*;
import java.io.*;

public class ClientTest{
	public static void main(String[] args){
		try{
			InetAddress address = InetAddress.getByName("localhost");
			Socket socketClient = new Socket(address, 2106);

			String message = null;

			OutputStream out = socketClient.getOutputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
			out.write("PORT 127,0,0,1,154,208\r\n".getBytes());

			while ( true ) {
				message = reader.readLine();
				if(message == null || message.length()<0) //verification message is ok
					break;
				if (message.equals("SYN")){
					System.out.println(message);
					System.out.println("SYN bien reçu, active fonctionne");
					out.write("SYN,ACK\r\n".getBytes());
					break;
				} 
					
			}//end of while

			while ( true ) {
				message = reader.readLine();
				if(message == null || message.length()<0) //verification message is ok
					break;
				if (message.equals("ACK")){
					System.out.println("ACK bien reçu, active fonctionne");
				} 
				if (message.equals("200 PORT command succesful")){
					System.out.println("200 PORT command succesful bien recu");
					break;
				}
					
			}//end of while
			out.write("USER Sam\r\n".getBytes());
			out.write("PASS 123456\r\n".getBytes());

			while ( true ) {
				message = reader.readLine();
				System.out.println(message);
				if(message == null || message.length()<0) //verification message is ok
					break;
				if (message.equals("230")){
					System.out.println("Mot de passe correct");
				} 
			}

			socketClient.close();
		}catch(UnknownHostException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

}