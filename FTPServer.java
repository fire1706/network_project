//---------------------------------FTPServer---------------------------------------------
//
// This first function goal is to launch the ftp server (in his globality).
// It will create the virtual file asked and allow us to launch a fixed
// number of threads when program is called.
//
//Copyright (c) 2019 by Thomas BASTIN & Victor Dachet. All Rights Reserved.
//-----------------------------------------------------------------------------------------

import java.util.*;
import java.lang.*;
import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.net.*;
import java.awt.*;
import javax.imageio.ImageIO;

public class FTPServer{

  public static void main(String[] args){

    // Firstly we take as an argument the Maxthreadpool
    int maxThread = 1 ;// We initiate is value to one

    // We effectuate this into a try and catch in the case that args cannot be parse correctly
    try{

      maxThread = Integer.parseInt(args[0]);

    }catch(NumberFormatException e){
      System.out.println("We couldn't parse correctly the maxThreads number ! ");
    }// end try&catch
    // We fixe the number thread pool
    ExecutorService exe = Executors.newFixedThreadPool(maxThread);

    // Instantiation of virtual file
    FileVirtuel file_virtuel = new FileVirtuel();


    try{
      // We create a ServerSocket with port 21xx for the connection
      ServerSocket commandSocket = new ServerSocket(2106);
      commandSocket.setSoTimeout(1000000);
      Socket managementCommandSocket = null;

      while(true){
        // We accpet the ServerSocket
        managementCommandSocket = commandSocket.accept();
        managementCommandSocket.setSoTimeout(300000); // timeout define to wait x second , if it is reach the Socket closed


        // We say that the server is ready to get command and data
        System.out.println("Ok let's go ");

        // We launch a new thread
        System.out.println("New connection incoming");
        
        exe.execute(new Management(managementCommandSocket, file_virtuel));
        
        
      }// end while


    }catch(IOException e){
      System.out.println(e.getMessage());
    }// end try&catch
    exe.shutdown();
  }//end main function
}// end class































    // A  faire : on doit ouvrir le socket du server puis dans un try and catch
    //  recevoir les donnée du client et le lancer ou sur un connection passive ou active
    // ensuite ouvrir autre fonction avec un extends threads ou le client se connecte
    // et fais ces truc ( en gros faire un run() ) lorsque que celui-ci se connecte
    // on lui donne un certain argument pour voir si il peut oui ou non aller voir les fichier privé
    // Notre Protocol devra pourvoir faire plusieurs truc on va donc implémenter
    // ou une class contenant plusieurs function utile ou alors une class pour chaque
    // fonction .( perso je pense une classe pour chaque remplie de petit opération définie dans une plus grand class avec des function pour chacune de ces petite opération)
    //
    //Attention a pas oublier les commentaire dans les codes et une intro a chque code
    //
    // ne pas oublier les opérations dans l'assignement page 6 (au dessus)
    //
    // c'est tout ce que j'ai a dire a toi Vic
    //
    //
