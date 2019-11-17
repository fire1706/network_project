// c'est Le run ou chque fonction est appelé pour faire ce qu'elle a a faire

import java.io.*;
import java.net.*;
import java.lang.*;
import java.util.*;

public class Management extends Thread {

    Socket socketManagement;
    Game ( Socket _s){socketManagement=_s;}

    @Override
    public void run(){
      try{
        System.println.out('Let''s start the game !');

        input = new BufferedReader(socketManagement.getInputStream());
        output= new PrintWriter(socketManagement.getOutputStream());
        input.readLine();

        

      }
