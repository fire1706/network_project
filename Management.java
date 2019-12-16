//---------------------------------Management---------------------------------------------
//
// The goal of this code is just to be a major program that will call other
// code in a good way to achieve what the client want to do.
//
//Copyright (c) 2019 by Thomas BASTIN & Victor Dachet. All Rights Reserved.
//-----------------------------------------------------------------------------------------



import java.util.*;
import java.lang.*;
import java.io.*;
import java.net.*;
import java.awt.*;
import javax.imageio.ImageIO;

public class Management extends Thread {

    Socket socketManagement = null;
    FileVirtuel file_virtuel = null;

  Management ( Socket _s, FileVirtuel file_virtuel){
    this.socketManagement=_s;
    this.file_virtuel = file_virtuel;

  }// End Management's constructor

      @Override
      public void run(){
        try{

          System.out.println("Let's start the game !");

          //-------------------------Connection du port 21xx-----------------------------------
          InputStream inCommandStream = socketManagement.getInputStream();
          OutputStream outCommandStream = socketManagement.getOutputStream();

          BufferedReader inputCommand = new BufferedReader(new InputStreamReader(inCommandStream));

          outCommandStream.write("220 server ready\r\n".getBytes());
          //------------------------------------------------------------------------------------


          //--------------------------Authentification------------------------------------------
          System.out.println("Let's do the authentification");

          Authentification authentification = new Authentification(socketManagement);
          int typeOfConnection = authentification.authented(); // =O normal , =1 can access private folder
          System.out.println("type of connection: " + typeOfConnection);
          //------------------------------------------------------------------------------------

          //---------------------------File Gestion---------------------------------------------
          FileGestion fileGestion = new FileGestion(socketManagement, file_virtuel, typeOfConnection);
          fileGestion.menu();
          //------------------------------------------------------------------------------------

          socketManagement.close();

          }catch(IOException e){
            System.out.println(e.getMessage());
          }// End try&catch
        }// End run method
      }// End class Management
