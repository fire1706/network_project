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
