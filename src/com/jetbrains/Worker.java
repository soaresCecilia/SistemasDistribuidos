package com.jetbrains;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Worker implements Runnable {

    private Socket clSock;
    private int id;
    private Repositorio repositorio;

    public Worker(Socket clsock, Repositorio repositorio){
        this.clSock =  clsock;
        this.repositorio= repositorio;
    }

    public void run(){
        try {
            if (clSock == null) System.out.println("clSock null");

            //lê do canal
            BufferedReader in = new BufferedReader(new InputStreamReader(this.clSock.getInputStream()));
            //escreve no canal
            PrintWriter out = new PrintWriter(clSock.getOutputStream());

            //passa o que li do canal para uma string
            String inComing=in.readLine();

             String[] comandos = inComing.split(" ");



            while(inComing!=null){



                switch (comandos[0]){
                    case "login":
                        //como faço login no repositorio??
                        repositorio.login(comandos[1],comandos[2]);

                        out.println("1");
                        out.flush();
                        break;

                    case "logout":
                        //vale a pena manter no repositorio quem está login?
                        out.println("1");
                        out.flush();
                        break;

                    case "registar":
                        repositorio.registar(comandos[1],comandos[2]);
                        out.println("1");
                        out.flush();
                        break;

                    case "procurarID":

                        int idM= Integer.parseInt(comandos[1]);
                        Musica m = repositorio.getMusicaId(idM);
                        out.println(m.toString());
                        out.flush();
                        break;
                    case "procurarPar":

                        List<Musica> lm=repositorio.getMusicasPar(comandos[1]);
                        int tam = lm.size();

                        out.println(""+tam);

                        for(Musica musica: lm){
                            out.println(musica.toString());
                        }

                        out.flush();

                        break;

                    case "upload":
                            repositorio.adicionaM(comandos[1]);
                            out.println("1");
                            out.flush();
                            break;
                    case "download":

                        out.println("ServidorWorker encontrou ??? asdad");
                        out.flush();
                        break;

                    default:
                        out.println("Opcao invalida");
                        out.flush();
                }

                inComing = in.readLine();
                comandos = inComing.split(" ");
            }
            clSock.shutdownOutput();
            clSock.shutdownInput();
            clSock.close();
        }
        catch (IOException e){}
    }

}


