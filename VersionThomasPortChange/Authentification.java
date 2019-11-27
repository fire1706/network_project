// ce programme a pour but de gérer l'autehentification de l'user :
// celui-ci peut etre anonyme ou être SAM ( on peut aussi se rajouter pour le fun)
// en fonction de l'identification de l'user on lui donnera une valeur qui déterminera
// si il peut accéder oui ou non au fichier noté comme étant privé !
// crée un choix au début pour se connecter en tant que user ou anonyme
// si user demander mot de passe si anonyme lancer le programme directement



import java.util.*;
import java.lang.*;
import java.io.*;
import java.net.*;
import java.awt.*;
import javax.imageio.ImageIO;

public class Authentification{
  public int authented(Socket connection){
    // On va crée un menu pour que la personne se connecter
    try{
      System.out.println("Please connect as anonymous if you don't have user account ! ");// in other way if you are not Sam
      InputStream inStream = connection.getInputStream();
      OutputStream outStream = connection.getOutputStream();

      while(true){
        BufferedReader input = new BufferedReader(new InputStreamReader(inStream));
        String inString = input.readLine();

        if(inString.startsWith("USER")){
          if(inString.contains("Anonymous")){
            outStream.write("230\r\n".getBytes());
            return 0; // for connection that cannot access private part
          }
          else if(inString.contains("Sam")){
            outStream.write("331\r\n".getBytes());
            // ATTENTION ne pas ré-instancier le bufferedReader sinon on perd le STREAM
            //input = new BufferedReader(new InputStreamReader(inStream));
            inString = input.readLine();
            System.out.println(inString);
            System.out.println("Checking password");
            if(inString.contains("PASS") && inString.contains("123456")){
              outStream.write("230\r\n".getBytes());
              return 1;
            }else{
              outStream.write("430\r\n".getBytes());
              System.out.println("Retry for the authentification.");
            }
          }
        }
      }

    }catch(IOException e){
      System.out.println(e.getMessage());
    }
    return -1;
  }
}
