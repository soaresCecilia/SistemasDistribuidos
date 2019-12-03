package com.jetbrains;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

    private void caminhoServidor(Integer port){
        cStub = new servidorSTUB(port);
    }

    public static void start(Integer porto){

        Servidor servidor = new Servidor();
        servidor.caminhoServidor(porto);
        servidor.connect();

        try {
            cliente.comandos();
        } catch (IOException e) {
            cliente.connect();
        }
    }

    public static void main(String[] args){

       Servidor.start(12346);

    }
}


/* short port = 12346;
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
        */