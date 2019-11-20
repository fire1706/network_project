// gère la connection passive
import java.util.*;
import java.lang.*;
import java.io.*;
import java.net.*;
import java.awt.*;
import javax.imageio.ImageIO;
//
//          / \
//         / | \        Ne pas oublier de gèrer les exceptions
//        /  |  \        Et de mettre un port number correct demander lequel mettre , doit t'on le chnager après le SYN ?
//       /   |   \
//      /    |    \              A relire (pour victor)(le code quoi)
//     /     •     \
//    --------------

public class Passive{
  public int connetPASV(Socket connection,String inString){
    // On vérifie que la demande est bien pour un connection passive
    if(!(inString == "PASV\r\n")){
      return 0; // pour dire que la connection a échoué
    }
    try{
      InputStream inStream = connection.getInputStream();
      OutputStream outStream = connection.getOutputStream();
      InetAddress ipAdresss = connection.getInetAddress();
      String host = ipAdresss.getHostAddress();
      host = host.replace(".",",");
      // vérifier quel port number il faut envoyer
      String message = new String("227 Entering Passive Mode("+host+",122,8)\r\n");
      outStream.write(message.getBytes());


      while(true){
        BufferedReader input = new BufferedReader(new InputStreamReader(inStream));
        inString = input.readLine();

        if(inString == "SYN\r\n"){
          outStream.write("SYN,ACK\r\n".getBytes());
        }
        else if(inString == "ACK\r\n"){
          System.out.println("Passive Connection established with succes!");
          return 1;
        }
      }
    }catch(IOException e){
      System.out.println(e.getMessage());
    }
    return 0;
  }
}
