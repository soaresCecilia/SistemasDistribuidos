package com.jetbrains.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    private static ServerHelper serverHelper;


   public static void start(Integer port, int nThreads, Repositorio repositorio) {
        try {

            System.out.println("Servidor esta a ligar...");
            ServerSocket serverSocket = new ServerSocket(port);


            while (!serverSocket.isClosed()) {
                Socket clSocket = serverSocket.accept();

                System.out.println("liguei!");
                ServerHelper serverHelper = new ServerHelper(clSocket, repositorio);
                Thread t = new Thread( new Worker(clSocket, serverHelper)); //cria uma thread por cliente
                t.start();

            }



        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            System.out.println(e.toString());
        }
        System.out.println("Desligado");

    }

    public static void main(String[] args){

        Repositorio repositorio = new Repositorio();

        Servidor.start(12346, 10, repositorio);

    }
}

