// gère la connection active

// gère la connection passive
import java.util.*;
import java.lang.*;
import java.io.*;
import java.net.*;
import java.awt.*;
import javax.imageio.ImageIO;

//          / \
//         / | \        Ne pas oublier de gèrer les exceptions
//        /  |  \        Et de mettre un port number correct demander lequel mettre , doit t'on le chnager après le SYN ?
//       /   |   \             Comment être sur que l'on se connect bien au bon truc comme pour le shéma du assignement demander absolument
//      /    |    \              A relire (pour victor)(le code quoi)
//     /     •     \
//    --------------

public class Active{
  public int connetACTV(Socket connection,String inString){
    // On vérifie que la demande est bien pour un connection passive
    if(inString == "PASV\r\n"){ // voir si il faudrait pas regarder plus en détail ce qui est envoyer
      return 0; // pour dire que la connection a échoué
    }
    // Comment se connecter au port donner ?

    try{
      InputStream inStream = connection.getInputStream();
      OutputStream outStream = connection.getOutputStream();

      outStream.write("SYN".getBytes());


      while(true){
        BufferedReader input = new BufferedReader(new InputStreamReader(inStream));
        inString = input.readLine();

        if(inString == "SYN,ACK\r\n"){
          outStream.write("ACK\r\n".getBytes());
          outStream.write("200 PORT command succesful".getBytes());
          System.out.println("Active Connection established with succes!");
          return 1;
        }
      }
    }catch(IOException e){
      System.out.println(e.getMessage());
    }
    return 0;
  }
}
