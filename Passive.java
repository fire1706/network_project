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
  public int connetPASV(Socket connection/*, Socket data*/, String inString){

    try{
      OutputStream outConnectionStream = connection.getOutputStream();
      InputStream inConnectionStream = connection.getInputStream();
      BufferedReader inputConnection = new BufferedReader(new InputStreamReader(inConnectionStream));
      //String inString = inputConnection.readLine();

      // On vérifie que la demande est bien pour un connection passive
      if(!(inString.contains("PASV") || inString.contains("EPSV"))){
        return 0; // pour dire que la connection a échoué
      }




      InetAddress ipAdresss = connection.getLocalAddress();
      InetAddress hostI = ipAdresss.getLocalHost();
      String host = hostI.getHostAddress();
System.out.println(host);
      host = host.replace(".",",");
      // vérifier quel port number il faut envoyer
      // on crée un port number random ( la probabilité est très très faible que l'on tombe sur un port déja utilisé)
      Random randNumber = new Random();
      int firstnum = randNumber.nextInt(156) +100;
      int secondnum = randNumber.nextInt(256);
      //Permier Message
      if(inString.contains("PASV")){
        String message = new String("227 Entering Passive Mode("+host+","+firstnum+","+secondnum+")\r\n");
        outConnectionStream.write(message.getBytes());
      }
      if(inString.contains("EPSV")){
        String message = new String("229 Entering Passive Mode("+host+","+firstnum+","+secondnum+")\r\n");
        outConnectionStream.write(message.getBytes());
      }
      int port = firstnum*256+secondnum; // a changer avec le port number que l'on utilise
      inString = inputConnection.readLine();
      if(inString.contains("EPRT")){
        System.out.println(inString);
        int n2 = inString.length() - 7 ;
        String getAddr = inString.substring(8,n2);
        System.out.println(getAddr);

        //outConnectionStream.write("200 \r\n".getBytes());
      }


      System.out.println(connection.getPort());
      ServerSocket newsocket = new ServerSocket(port);
      Socket data  = newsocket.accept();
      System.out.println(connection.getPort());
      InputStream inDataStream = data.getInputStream();
      OutputStream outDataStream = data.getOutputStream();

      //BufferedReader input = new BufferedReader();
      String inDataString = new String();

      while(true){
         BufferedReader input = new BufferedReader(new InputStreamReader(inDataStream));
        inDataString = input.readLine();

        if(inDataString == "SYN\r\n"){
          outDataStream.write("SYN,ACK \r\n".getBytes());
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
