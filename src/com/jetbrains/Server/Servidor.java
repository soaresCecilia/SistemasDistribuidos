package com.jetbrains.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    private static ServerHelper serverHelper;


   public static void start(Integer port, int nThreads, Repositorio repositorio) {
        try {

            System.out.println("Servidor esta a iniciar...");
            ServerSocket serverSocket = new ServerSocket(port);

            while (!serverSocket.isClosed()) {
                Socket clSocket = serverSocket.accept();

                System.out.println("liguei!");
                ServerHelper serverHelper = new ServerHelper(clSocket, repositorio);
                Thread t = new Thread(new Worker(clSocket, repositorio)); //tirar o socket do worker cria uma thread por cliente
                t.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e){
            System.out.println(e.toString());
        }
        System.out.println("Desligado");

    }

    public static void teste(Repositorio repositorio) {
        Musica m1 = new Musica (1, "ola", "viva", "1982","rock", "ficheiro", 9);
        Musica m2 = new Musica (2, "vi", "Ze Cabra", "1999","pimba", "ficheiro", 19);
        Musica m3 = new Musica (3, "vida bandida", "Ana Carolina", "2002","soul", "ficheiro", 9);
        Musica m4 = new Musica (4, "vira do minho", "Grupo os amores", "2010","pimba", "ficheiro", 19);

        repositorio.addMusica(m1);
        repositorio.addMusica(m2);
        repositorio.addMusica(m3);
        repositorio.addMusica(m4);

        for(Musica m : repositorio.getMusicas().values())
            System.out.println(m.toString());
    }

    public static void main(String[] args){

        Repositorio repositorio = new Repositorio();

        //teste(repositorio);

        Servidor.start(12346, 10, repositorio);
    }
}

