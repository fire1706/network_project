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
  public int connetPASV(Socket connection, Socket data, String inString){
    // On vérifie que la demande est bien pour un connection passive
    if(!(inString == "PASV\r\n")){
      return 0; // pour dire que la connection a échoué
    }
    try{
      OutputStream outConnectionStream = connection.getOutputStream();
      InetAddress ipAdresss = connection.getInetAddress();
      String host = ipAdresss.getHostAddress();
      host = host.replace(".",",");
      // vérifier quel port number il faut envoyer
      // on crée un port number random ( la probabilité est très très faible que l'on tombe sur un port déja utilisé)
      Random randNumber = new Random();
      int firstnum = randNumber.nextInt(156) +100;
      int secondnum = randNumber.nextInt(256);
      //Permier Message
      String message = new String("227 Entering Passive Mode("+host+","+firstnum+","+secondnum+")\r\n");
      outConnectionStream.write(message.getBytes());
      int port = firstnum*256+secondnum; // a changer avec le port number que l'on utilise

System.out.println("je suis ici !");

      System.out.println(connection.getPort());
      ServerSocket newsocket = new ServerSocket(port);
      data  = newsocket.accept();
      System.out.println(connection.getPort());
      InputStream inDataStream = data.getInputStream();
      OutputStream outDataStream = data.getOutputStream();

      //BufferedReader input = new BufferedReader();
      String inDataString = new String();

      while(true){
         BufferedReader input = new BufferedReader(new InputStreamReader(inDataStream));
        inDataString = input.readLine();

        if(inDataString == "SYN\r\n"){
          outDataStream.write("SYN,ACK\r\n".getBytes());
        }
        else if(inDataString == "ACK\r\n"){
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
