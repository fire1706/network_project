import java.util.*;
import java.lang.*;
import java.io.*;
import java.net.*;
import java.awt.*;
import javax.imageIO.*;

public class FTPServer{

  public static void main(String[] args) throws Exception{

    // Firstly we take as an argument the Maxthreadpool
    int maxThreads = 1;// We initiate is value to one
    try{
      int maxThreads = Integer.parseInt(args[0]);
    }catch(NumberFormatException e){
      System.out.println("We couldn't parse correctly the maxThreads number ! ");
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
