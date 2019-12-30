package com.jetbrains.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

    private static ServerHelper serverHelper;
    private static String caminhoFicheiro = "/home/luisabreu/Desktop/musicaS/";
    //private static String caminhoFicheiro = "/Users/cecilia/Desktop/musicas/servidor/";
    //private static String caminhoFicheiro = null;
    //private static String caminhoFicheiro = "null";



   public static void start(Integer port, int nThreads, Repositorio repositorio) {
        try {

            System.out.println("Servidor esta a iniciar...");
            ServerSocket serverSocket = new ServerSocket(port);

            while (!serverSocket.isClosed()) {
                Socket clSocket = serverSocket.accept();

                System.out.println("liguei!");
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

        Musica m1 = new Musica (1,"ola", "viva", "1982","rock", caminhoFicheiro+"ola.mp3", 9);
        Musica m2 = new Musica (2,"vi", "Ze Cabra", "1999","pimba", caminhoFicheiro+"vi.mp3", 9 );
        Musica m3 = new Musica (3, "vida bandida", "Ana Carolina", "2002","soul",  caminhoFicheiro+"vida bandida.mp3", 4);
        Musica m4 = new Musica (4, "vira do minho", "Grupo os amores", "2010","pimba", caminhoFicheiro+"vira do minho.mp3", 5);



        Utilizador luis = new Utilizador("luis","102030");
        Utilizador cecilia = new Utilizador("cecilia", "102030");
        Utilizador alexandre = new Utilizador("alexandre","102030");
        Utilizador miriam = new Utilizador("miriam", "102030");


        repositorio.addMusica(m1);
        repositorio.addMusica(m2);
        repositorio.addMusica(m3);
        repositorio.addMusica(m4);

        repositorio.addUtilizador(luis);
        repositorio.addUtilizador(cecilia);
        repositorio.addUtilizador(alexandre);
        repositorio.addUtilizador(miriam);

        for(Musica m : repositorio.getMusicas().values())
            System.out.println(m.toString());
    }

    public static void main(String[] args){

        Repositorio repositorio = new Repositorio();

        teste(repositorio);

        Servidor.start(12346, 10, repositorio);
    }
}

