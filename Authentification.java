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
  Socket connection = new Socket();

  public Authentification(Socket connection){
    this.connection = connection;
  }
  public int authented(/*Socket connection/*,String firstString*/){
    // On va crée un menu pour que la personne se connecter
    try{
      System.out.println("Please connect as anonymous if you don't have user account ! ");// in other way if you are not Sam
      InputStream inStream = connection.getInputStream();
      OutputStream outStream = connection.getOutputStream();
      String inString = new String();

      int i = 1;
BufferedReader input = new BufferedReader(new InputStreamReader(inStream));
      while(true){
      /*  if(i == 1){
            inString = firstString;
        }else{*/
        String b1 = String.valueOf(connection.isConnected());
        String b2 = String.valueOf(connection.isInputShutdown());
        System.out.println("\n\r"+b1+" hey  hey "+b2);
//System.out.println("yoda maitre tu seras");

        while( (inString = input.readLine()) == null){}

          //inString = input.readLine();// impossible de lire ce message piour la connextion

      //  }
System.out.println("ici je suis , mandalorian :"+inString);
 b1 = String.valueOf(connection.isConnected());
 b2 = String.valueOf(connection.isInputShutdown());
System.out.println("\n\r"+b1+" hey and hey "+b2);



        if(inString.startsWith("USER")){
          if(inString.contains("anonymous")){
            outStream.write("230 you are connected\r\n".getBytes());
            return 0; // for connection that cannot access private part
          }
          else if(inString.contains("Sam")){
            outStream.write("331 waiting for password\r\n".getBytes());

            // ATTENTION ne pas ré-instancier le bufferedReader sinon on perd le STREAM
            /*System.out.println(inString);
            input = new BufferedReader(new InputStreamReader(inStream));
            System.out.println(inString);
            inString = input.readLine();
            System.out.println(inString);*/
            System.out.println("Checking password");
          }}
          // check password
        else if(inString.startsWith("PASS") && inString.contains("123456")){
              outStream.write("230 you are connected\r\n".getBytes());
              return 1;
        }else{
              outStream.write("430\r\n".getBytes());
              System.out.println("Retry for the authentification.");
            }

      }


    }catch(IOException e){
      System.out.println(e.getMessage());
    }
    return -1;
  }
}
