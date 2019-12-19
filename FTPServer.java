//---------------------------------FTPServer---------------------------------------------
//
// This first function goal is to launch the ftp server (in his globality).
// It creates the virtual files asked and allows us to launch a fixed
// number of threads when the program is called.
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
    // We fix the number thread pool
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
