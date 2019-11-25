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
				if (message.startsWith("SYN")){
					System.out.println("SYN bien reçu, active fonctionne");
					break;
				} 
					
			}//end of while

			socketClient.close();
		}catch(UnknownHostException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

}