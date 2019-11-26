package com.jetbrains;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    public static void main(String[] args){
        short port = 12345;
        //int idThread=0;
        Repositorio repositorio = new Repositorio();

        try {

            ServerSocket sSock = new ServerSocket(port);
            if(sSock==null) System.out.println("sSock do Server é null");
            while (true) {

                Socket clSock = sSock.accept();
                if(clSock==null) System.out.println("clSock do Server é null");

                Thread t = new Thread(new Worker(clSock,repositorio));
                //t.start();
                //idThread++;

            }
        }
        catch (IOException e ){
            e.printStackTrace();
        }

    }
}
