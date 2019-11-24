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

          int isconnected = 0;
          while(isconnected == 0){
            if(inString == "PASV\r\n"){
              // appeler le truc passif
            	Passive pass = new Passive();
            	isconnected =  pass.connetPASV(socketManagement, inString);// ce truc si gènére un erreur mais je comprend pas pourquoi , je pense que je l'appelle mal mais je m'embrouille avec ces truc la si tu veux bien y regarder mon petit victor ca m'arrangerait ;)
              //ca fonctionne pas car tu appelles une méthode située dans une autre classe... Il faut ou bien instancier un objet ou créer ces méthodes (active/passive ici)
            }else{
              // appeler le truc actif
            	Active act = new Active();
            	isconnected = act.connetACTV(socketManagement, inString);//idem que ligne (cette ligne)-3;
            }
          }
          System.out.println("Client is connected");

          System.out.println("Let's do the authentification");
          int typeOfConnection = 0; // O normal , 1 can access private folder
          //Appeler la fonction Athentification

          // Pour la suite crée un menu et répondre au demande du client avec les fonction de FileGestion


        }catch(IOException e){
          System.out.println(e.getMessage());
        }
      }
    }/*);
  }*/
