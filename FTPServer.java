import java.util.*;
import java.lang.*;
import java.io.*;
import java.net.*;
import java.awt.*;
import javax.imageio.ImageIO;
// ICI le serveur va seulement ouvirr un nouveau thread pour chque connection
// attetion a gérer le  nombre thread maximum cfr section 3 de l'assingment

public class FTPServer{

  public static void main(String[] args) throws Exception{// pas sur que on doit garder le throws exception

    // Firstly we take as an argument the Maxthreadpool
    int maxThread = 1 ;// We initiate is value to one
    try{
      maxThread = Integer.parseInt(args[0]);

    }catch(NumberFormatException e){
      System.out.println("We couldn't parse correctly the maxThreads number ! ");
    }



    try{
      ServerSocket commandSocket = new ServerSocket(2106);// mis ici car c'est le port par défaut
      commandSocket.setSoTimeout(1000000);



      while(true){
        //ServerSocket dataSocket = new ServerSocket(2006); // ce socket par contre va etre changer si PASV
        //dataSocket.setSoTimeout(1000000);

        // initiation des commandes du socket gérant les comandes
        Socket managementCommandSocket = commandSocket.accept();

        managementCommandSocket.setSoTimeout(728242);
        //InputStream inCommandStream = managementCommandSocket.getInputStream();
        OutputStream outCommandStream = managementCommandSocket.getOutputStream();
        //BufferedReader inputCommand = new BufferedReader(new InputStreamReader(inCommandStream));

        //PrintWriter outputCommand = new PrintWriter(outCommandStream);
        outCommandStream.write("220 server ready\r\n".getBytes());
        //String inCommandString = inputCommand.readLine();
        System.out.println(managementCommandSocket.getPort()+"ici");
//System.out.println(inCommandString);





        // initiation des commandes du socket gérant les data
      //  Socket managementDataSocket = dataSocket.accept();// problème ici putaint


      //  managementDataSocket.setSoTimeout(728242);
        /*InputStream inDataStream = managementDataSocket.getInputStream();
        OutputStream outDataStream = managementDataSocket.getOutputStream();
        BufferedReader inputData = new BufferedReader(new InputStreamReader(inDataStream));
        PrintWriter outputData = new PrintWriter(outDataStream);
        String inDataString = inputData.readLine();
        System.out.println(managementDataSocket.getPort());*///pas sur que c'est utile de mettre tout ca

          System.out.println("Ok let's go ");

/*        int isconnected = 0;
        while(isconnected == 0){
          if(inCommandString == "PASV\r\n"){
            // appeler le truc passif
            System.out.println("passive");
            Passive pass = new Passive();
            isconnected =  pass.connetPASV(managementCommandSocket, managementDataSocket, inCommandString);// ce truc si gènére un erreur mais je comprend pas pourquoi , je pense que je l'appelle mal mais je m'embrouille avec ces truc la si tu veux bien y regarder mon petit victor ca m'arrangerait ;)
            //ca fonctionne pas car tu appelles une méthode située dans une autre classe... Il faut ou bien instancier un objet ou créer ces méthodes (active/passive ici)
          }else if(inCommandString == null || inCommandString.length()<0){
            System.out.println("Mauvaise Réception du message dans le InputStream");
            outCommandStream.write("120\r\n".getBytes());
            //attention il faut faire un truc en plus pour gérer ce cas mais pas tout de suite

          }else{
            // appeler le truc actifs
            System.out.println("active");
            Active act = new Active();
            isconnected = act.connetACTV(managementCommandSocket, managementDataSocket, inCommandString);//idem que ligne (cette ligne)-3;
          }

        }*/





        Management newconnection = new Management(managementCommandSocket/*, managementDataSocket,maxThread*/);// pas sur duqeul il faut envoyer
        System.out.println("New connection incoming");
        newconnection.start();

      }
    }catch(IOException e){
      System.out.println(e.getMessage());
    }
  }
}































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
