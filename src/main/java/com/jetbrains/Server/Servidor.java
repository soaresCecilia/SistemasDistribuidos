package com.jetbrains.Server;

import com.jetbrains.Server.Dados.Musica;
import com.jetbrains.Server.Dados.Repositorio;
import com.jetbrains.Server.Dados.Utilizador;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

public class Servidor {

    private static final ThreadPool threadPool = new ThreadPool();
    private static final String caminhoFicheiro = "/tmp/servidor_soundcloud/";
    private static int workerThread  = 0;

   public static void start(Integer port, Repositorio repositorio) {
       Logger logger = Logger.getLogger("Servidor");

        try {
            System.out.println("Servidor esta a iniciar...");
            ServerSocket serverSocket = new ServerSocket(port);

            System.out.println("Servidor iniciado...");
            while (!serverSocket.isClosed()) {
                Socket clSocket = serverSocket.accept();

                logger.debug("Conexao estabelecida: " + clSocket.getInetAddress());
                Thread t = new Thread(new Worker(clSocket, repositorio, threadPool)); //tirar o socket do worker cria uma thread por cliente
                t.setName("Worker Thread " + workerThread);
                workerThread++;
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

        Musica m1 = new Musica (1,"ola", "Elton_zJohn", "1982","rock", caminhoFicheiro+"ola.mp3", 9);
        Musica m2 = new Musica (2,"vi", "Ze_Cabra", "1999","pimba", caminhoFicheiro+"vi.mp3", 9 );
        Musica m3 = new Musica (3, "vida_bandida", "Ana_Carolina", "2002","soul",  caminhoFicheiro+"vida_bandida.mp3", 4);
        Musica m4 = new Musica (4, "vira_do_minho", "Grupo_os_amores", "2010","pimba", caminhoFicheiro+"vira_do_minho.mp3", 5);

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

    private static void initLogger() {
        Logger rootLogger = Logger.getRootLogger();
        PatternLayout layout = new PatternLayout("[%-5p] %t:%c - %m%n");

        try {
            RollingFileAppender fileAppender = new RollingFileAppender(layout, System.getProperty("log.file", "./servidor-ssd.log"));
            fileAppender.setImmediateFlush(true);
            fileAppender.setThreshold(Level.DEBUG);
            fileAppender.setAppend(true);
            rootLogger.addAppender(fileAppender);
        } catch (IOException e) {
            rootLogger.error("Falha ao configurar o logging.", e);
        }
    }

    public static void main(String[] args){

        initLogger();

        Repositorio repositorio = new Repositorio();

        teste(repositorio);

        Servidor.start(12346,repositorio);
    }
}

