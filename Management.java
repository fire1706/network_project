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

  Management ( Socket _s/*, Socket _s2, Integer maxthreads*/){socketManagement=_s;/*, socketData=_s2,Maxthreads = maxthreads;*/}

      @Override
      public void run(){
        try{
          System.out.println("Let's start the game !");
 

          //-------------------------Connection du port 21xx-----------------------------------
          InputStream inCommandStream = socketManagement.getInputStream();
          OutputStream outCommandStream = socketManagement.getOutputStream();
/*
          BufferedReader inputCommand = new BufferedReader(new InputStreamReader(inCommandStream));
          PrintWriter outputCommand= new PrintWriter(outCommandStream);
          String inCommandString = inputCommand.readLine();*/
          //-----------------------------------------------------------------------------------


          //--------------------------Authentification------------------------------------------
              System.out.println("Let's do the authentification");

          Authentification authentification = new Authentification();
          int typeOfConnection = authentification.authented(socketManagement/*,inCommandString*/); // O normal , 1 can access private folder
          System.out.println(typeOfConnection);
          //------------------------------------------------------------------------------------


          //-------------------------Connection du port 20xx-----------------------------------
          ServerSocket dataSocket = new ServerSocket(2006); // ce socket par contre va etre changer si PASV
          dataSocket.setSoTimeout(1000000);

          Socket socketData = dataSocket.accept();// problème ici a régler
          socketData.setSoTimeout(728242);
          //-----------------------------------------------------------------------------------


            //inCommandString = inputCommand.readLine();

          //---------------------------Connection-----------------------------------------------
          System.out.println("Client is connected");
/*
          int isconnected = 0;
          while(isconnected == 0){
            if(inCommandString == "PASV\r\n"){
              // appeler le truc passif
              Passive pass = new Passive();
              isconnected =  pass.connetPASV(socketManagement,socketData, inCommandString);// ce truc si gènére un erreur mais je comprend pas pourquoi , je pense que je l'appelle mal mais je m'embrouille avec ces truc la si tu veux bien y regarder mon petit victor ca m'arrangerait ;)
              //ca fonctionne pas car tu appelles une méthode située dans une autre classe... Il faut ou bien instancier un objet ou créer ces méthodes (active/passive ici)
            }else if(inCommandString == null || inCommandString.length()<0){
              System.out.println("Mauvaise Réception du message dans le InputStream");
              //attention il faut faire un truc en plus pour gérer ce cas mais pas tout de suite

            }else{
              // appeler le truc actif
              Active act = new Active();
              isconnected = act.connetACTV(socketManagement,socketData, inCommandString);//idem que ligne (cette ligne)-3;
            }
          }*/
          //------------------------------------------------------------------------------------


          /*Authentification authentification = new Authentification();
          int typeOfConnection = authentification.authented(socketManagement); // O normal , 1 can access private folder
          System.out.println(typeOfConnection);*/

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
