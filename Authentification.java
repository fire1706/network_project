//---------------------------------Authentification---------------------------------------------
//
// This function is used to do the identification of the USER. This one can be
// Anonymous or the single user Sam. The anonymous account doesn't need a password
// but the account of Sam as one and it is 123456.
//
//Copyright (c) 2019 by Thomas BASTIN & Victor Dachet. All Rights Reserved.
//-----------------------------------------------------------------------------------------

import java.util.*;
import java.lang.*;
import java.io.*;
import java.net.*;
import java.awt.*;
import javax.imageio.ImageIO;

public class Authentification{
  Socket connection = new Socket();

  // Constructor that take a socket as argument
  public Authentification(Socket connection){
    this.connection = connection;
  }// end Constructor

  // Method use for the authentification
  public int authented(){

    // As we are using stream we put this in a try&catch
    try{
      System.out.println("Please connect as anonymous if you don't have user account ! ");// in other way if you are not Sam
      InputStream inStream = connection.getInputStream();
      OutputStream outStream = connection.getOutputStream();
      String inString = new String();

      int i = 1;
      BufferedReader input = new BufferedReader(new InputStreamReader(inStream));
        while(true){

          while( (inString = input.readLine()) == null){}

          // We first check the USER
          if(inString.startsWith("USER")){
            if(inString.contains("anonymous")){
              outStream.write("230 you are connected\r\n".getBytes());
              return 0; // for connection that cannot access private part
            }
            else if(inString.contains("Sam")){
              outStream.write("331 waiting for password\r\n".getBytes());

              System.out.println("Checking password");
            }// end second if
          }
          // Now we check the password
          else if(inString.startsWith("PASS") && inString.contains("123456")){
              outStream.write("230 you are connected\r\n".getBytes());
              return 1;
          }else if(inString.contains("AUTH")){
    					outStream.write("502 Command not implemented\r\n".getBytes());
          }else{
              outStream.write("430 Retry for the authentification\r\n".getBytes());
              System.out.println("Retry for the authentification.");

          }// end First if

      }// end While


    }catch(IOException e){
      System.out.println(e.getMessage());
    }// end try&catch
    return -1;
  }// end method
}// end Class
