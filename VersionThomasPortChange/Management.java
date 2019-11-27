import java.util.*;
import java.lang.*;
import java.io.*;
import java.net.*;
import java.awt.*;
import javax.imageio.ImageIO;

public class Management extends Thread {
  /*ExecutorService exe = Executors.newFixedThreadPool(maxThread);
  exe.execute(new Runnable() {
    Integer Maxthreads*/ // revoir la partie avec le thread pool j'ai du mal a l'utiliser correctement
    Socket socketManagement;
  Management ( Socket _s/*, Integer maxthreads*/){socketManagement=_s;/*Maxthreads = maxthreads;*/}

      @Override
      public void run(){
        try{
          System.out.println("Let's start the game !");

          InputStream inStream = socketManagement.getInputStream();
          OutputStream outStream = socketManagement.getOutputStream();

          BufferedReader input = new BufferedReader(new InputStreamReader(inStream));
          PrintWriter output= new PrintWriter(outStream);
          String inString = input.readLine();

          
          System.out.println("Client is connected");

          System.out.println("Let's do the authentification");

          Authentification authentification = new Authentification();
          int typeOfConnection = authentification.authented(socketManagement); // O normal , 1 can access private folder
          System.out.println(typeOfConnection);
          // Pour la suite crée un menu et répondre au demande du client avec les fonction de FileGestion
          if(typeOfConnection == 0 || typeOfConnection == 1){
            FileGestion fileGestion = new FileGestion(socketManagement, typeOfConnection);
            fileGestion.menu();
          }else{
            System.out.println("Access denied");
            System.out.println("The connection will be closed");
          }

          socketManagement.close();


        }catch(IOException e){
          System.out.println(e.getMessage());
        }
      }
    }/*);
  }*/
