import java.util.*;
import java.lang.*;
import java.io.*;
import java.net.*;
import java.awt.*;
import javax.imageio.ImageIO;

// Le but de cette fonction et juste de faire ce que je sais et puis on pourra venir les chercher et faire un copier coller ;)
//https://docs.oracle.com/javase/8/docs/api/java/io/File.html#delete--
// attention ces fonction n'ont pas encore été tester avec des test réel


// Attention que j'ai pas gérer le fais que les truc s'envoie dans le outputstream ou alors des truc se recoivent !
// Parce que je pensais que on allait gérer ca allieurs et ceci se sont juste des petit fonction a appeler pour aider 
//et pas avoir un code surchager maintneant je peux faire en sorte que ca gère les outstream et tout et tout
public class listeFonctionFlieParThomas{

public void CDUP(){

}

public void CWD(){

}

public String[] LIST(File fichier){
  try{
    return fichier.list();
  }catch(SecurityException e){
    System.out.println(e.getMessage());
  }
  return null;
}

public String[] LIST(File fichier, FilenameFilter name){
  try{
    return fichier.list(name);
  }catch(SecurityException e){
    System.out.println(e.getMessage());
  }
  return null;
}

public File[] LIST(File fichier, int one){
  try{
    return fichier.listFiles();
  }catch(SecurityException e){
    System.out.println(e.getMessage());
  }
  return null;
}

public File[] LIST(File fichier, FilenameFilter name, int one){
  try{
    return fichier.listFiles(name);
  }catch(SecurityException e){
    System.out.println(e.getMessage());
  }
  return null;
}

public String PWD(File fichier){
  try{
    return fichier.getParent();
  }catch(Exception e){
    System.out.println(e.getMessage());
  }
  return null;
}

public File PWD(File fichier,int one){// le int est pour spécifier que c'est une mthédoe différente
  try{
    return fichier.getParentFile();
  }catch(Exception e){
    System.out.println(e.getMessage());
  }
  return null;
}

public File Download(File fichier){// Download donc on prend le fichier en entier et on le mets dans le return pour qu'il puisse être envoyer arpès
  try{
    return fichier.getAbsoluteFile();
  }catch(SecurityException e){
    System.out.println(e.getMessage());
  }
  return null;
}

public File Upload(File fichier,String child){// upload donc on crée un nouveau fichier a partir des données recue
  try{
    File fiche = new File(fichier,child);
    return fiche;
  }catch(NullPointerException e){
    System.out.println(e.getMessage());
  }
  return null;
}

// Fonction qui a pour but de renomer un fichier
public void Rename(File fichier, String newName){
  try{
    File newFichName = new File(newName);
    boolean isOK = false ;
    while(!(isOK)){
      isOK = fichier.renameTo(newFichName);
    }
    System.out.println("File rename with succes !");
  }catch(Exception e){
    System.out.println(e.getMessage());
  }
}

// Fonction qui a pour but de supprimer un fichier | Attention cela peut marcher avec un directory si celui-ci est vide
public void Delete(File fichier){
  System.out.println("A file is going to be deleted");
  try{
    boolean isOK = false ;
    while(!(isOK)){
      isOK = fichier.delete();
    }
    System.out.println("File deleted with succes !");
  }catch(Exception e){
    System.out.println(e.getMessage());
  }
}

public void SYST(){

}

public void FEAT(){

}

public void MDTM(){

}

}
