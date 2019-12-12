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
    Socket socketManagement = null;
    FileVirtuel file_virtuel = null;

  Management ( Socket _s/*, Socket _s2, Integer maxthreads*/, FileVirtuel file_virtuel){
    this.socketManagement=_s;
    this.file_virtuel = file_virtuel;
    /*, socketData=_s2,Maxthreads = maxthreads;*/
  }

      @Override
      public void run(){
        try{
          System.out.println("Let's start the game !");


          //-------------------------Connection du port 21xx-----------------------------------
          InputStream inCommandStream = socketManagement.getInputStream();
          OutputStream outCommandStream = socketManagement.getOutputStream();

          BufferedReader inputCommand = new BufferedReader(new InputStreamReader(inCommandStream));
          /*PrintWriter outputCommand= new PrintWriter(outCommandStream);
          String inCommandString = inputCommand.readLine();*/
          //-----------------------------------------------------------------------------------


          //--------------------------Authentification------------------------------------------
              System.out.println("Let's do the authentification");

          Authentification authentification = new Authentification(socketManagement);
          int typeOfConnection = authentification.authented(/*ÒÒsocketManagement/*,inCommandString*/); // O normal , 1 can access private folder
          System.out.println("type of connection: " + typeOfConnection);
          //------------------------------------------------------------------------------------


          //-------------------------Connection du port 20xx-----------------------------------
          /*ServerSocket dataSocket = new ServerSocket(2006); // ce socket par contre va etre changer si PASV
          dataSocket.setSoTimeout(1000000);

          Socket socketData = dataSocket.accept();// problème ici a régler
          socketData.setSoTimeout(728242);*/
          //-----------------------------------------------------------------------------------


            String inCommandString = inputCommand.readLine();
            if(inCommandString != null){
              while(true){
                System.out.println(inCommandString);
                if( inCommandString.contains("SYST")){
                  Properties prop = new Properties();
                  prop = System.getProperties();
                  String str = prop.getProperty("os.arch");
                  inCommandString = "215 "+str+"\r\n";
                  outCommandStream.write(inCommandString.getBytes());
                  inCommandString = inputCommand.readLine();
                }else if( inCommandString.contains("FEAT")){
                  outCommandStream.write("211 to do \r\n".getBytes());// a étofer il faut mettre les command possible
                  inCommandString = inputCommand.readLine();
                }else if( inCommandString.contains("PWD")){
                  outCommandStream.write("257 /\r\n".getBytes());
                  inCommandString = inputCommand.readLine();
                }else if( inCommandString.contains("TYPE")){
                  if(inCommandString.contains("I")){
                    outCommandStream.write("200 Type set to I\r\n".getBytes());
                  }
                  if(inCommandString.contains("A")){
                    outCommandStream.write("200 Type set to A\r\n".getBytes());
                  }
                  inCommandString = inputCommand.readLine();
                }else{
                  break;
                }
              }
            }

          //---------------------------Connection-----------------------------------------------


          int isconnected = 0;
          while(isconnected == 0){
            if(inCommandString.contains("PASV") || inCommandString.contains("EPSV")){
              // appeler le truc passif
              //System.out.println("Je suis ici mon gars");
              Passive pass = new Passive();
              isconnected =  pass.connetPASV(socketManagement/*,socketData*/, inCommandString);// ce truc si gènére un erreur mais je comprend pas pourquoi , je pense que je l'appelle mal mais je m'embrouille avec ces truc la si tu veux bien y regarder mon petit victor ca m'arrangerait ;)
              //ca fonctionne pas car tu appelles une méthode située dans une autre classe... Il faut ou bien instancier un objet ou créer ces méthodes (active/passive ici)
            }else if(inCommandString == null || inCommandString.length()<0){
              System.out.println("Mauvaise Réception du message dans le InputStream");
              //attention il faut faire un truc en plus pour gérer ce cas mais pas tout de suite

            }else{
              // appeler le truc actif
              Active act = new Active();
              isconnected = act.connetACTV(socketManagement/*,socketData*/, inCommandString);//idem que ligne (cette ligne)-3;
            }
          }
          System.out.println("Client is connected");
          //------------------------------------------------------------------------------------


          /*Authentification authentification = new Authentification();
          int typeOfConnection = authentification.authented(socketManagement); // O normal , 1 can access private folder
          System.out.println(typeOfConnection);*/

          // Pour la suite crée un menu et répondre au demande du client avec les fonction de FileGestion
          if(typeOfConnection == 0 || typeOfConnection == 1){
            FileGestion fileGestion = new FileGestion(socketManagement, typeOfConnection, file_virtuel);
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
