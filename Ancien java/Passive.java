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
  public ServerSocket connetPASV(Socket connection/*, Socket data*/, String inString){

    try{
      OutputStream outConnectionStream = connection.getOutputStream();
      InputStream inConnectionStream = connection.getInputStream();
      BufferedReader inputConnection = new BufferedReader(new InputStreamReader(inConnectionStream));
      ServerSocket data = new ServerSocket();
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
      if(inString.startsWith("PASV")){
        ServerSocket dataChannel = new ServerSocket(firstnum * 256 + secondnum);
        dataChannel.setSoTimeout(1000000);

      while(true){
        // We accpet the ServerSocket
        Socket serverData = dataChannel.accept();
        serverData.setSoTimeout(728242);

        OutputStream outDataChannel = serverData.getOutputStream();


        String message = new String("227 Entering Passive Mode("+host+","+firstnum+","+secondnum+")\r\n");
        outConnectionStream.write(message.getBytes());
      }
      if(inString.startsWith("EPSV")){
        String message = new String("229 Entering Passive Mode("+host+","+firstnum+","+secondnum+")\r\n");
        outConnectionStream.write(message.getBytes());
      }
      int port = firstnum*256+secondnum; // a changer avec le port number que l'on utilise
      inString = inputConnection.readLine();
      if(inString.contains("EPRT") || inString.contains("PORT")){
        outConnectionStream.write("200 \r\n".getBytes());
        return 1;
      /*  System.out.println(inString);
        int n2 = inString.length() - 7 ;
        String getAddr = inString.substring(8,n2);
        System.out.println(getAddr);
        data = new Socket(getAddr,1025,hostI,port);*/
      }


/*
      System.out.println(connection.getPort());
      ServerSocket newsocket = new ServerSocket(port);
      Socket data  = newsocket.accept();*/
      System.out.println(connection.getPort());
      InputStream inDataStream = data.getInputStream();
      OutputStream outDataStream = data.getOutputStream();

      //BufferedReader input = new BufferedReader();
      String inDataString = new String();
System.out.println(" ici ");
      while(true){
         BufferedReader input = new BufferedReader(new InputStreamReader(inDataStream));
        while((inDataString = input.readLine())==null){}
System.out.println(" ici ");
        if(inDataString.contains("SYN")){
System.out.println(" ici ");
          outDataStream.write("SYN,ACK \r\n".getBytes());
        }
        else if(inDataString.contains("ACK")){
          System.out.println("Passive Connection established with succes!");
          outConnectionStream.write("200 \r\n".getBytes());
          return 1;
        }
      }}

    }catch(IOException e){
      System.out.println(e.getMessage());
    }
    return 0;
  }
}
